<template>
  <div class="min-h-screen">
    <div class="relative">
      <div class="h-80 w-full overflow-hidden">
        <img :src="tripDetails.image" :alt="tripDetails.title" class="w-full h-full object-cover" />
        <div class="absolute inset-0 bg-black/30"></div>
      </div>

      <div class="absolute bottom-0 left-0 right-0 p-6 text-white">
        <div class="max-w-7xl mx-auto">
          <div class="flex items-center gap-2 mb-2">
            <span class="px-2 py-1 bg-white/20 backdrop-blur-sm rounded-full text-sm">{{
              getSidoName(Number(tripDetails.location)) 
            }}</span>
            <span class="px-2 py-1 bg-white/20 backdrop-blur-sm rounded-full text-sm"
              >{{ tripDetails.duration }}박 {{ tripDetails.duration + 1 }}일</span
            >
          </div>
          <h1 class="text-3xl md:text-4xl font-bold mb-2">{{ tripDetails.title }}</h1>
          <div class="flex items-center gap-3">
            <div class="flex items-center gap-2">
              <div
                class="w-8 h-8 rounded-full bg-white/30 backdrop-blur-sm flex items-center justify-center"
              >
                <span>{{ authorInfo.name ? authorInfo.name.charAt(0) : 'U' }}</span>
              </div>
              <span>{{ authorInfo.name || 'Unknown' }} ({{ authorInfo.userId || 'unknown' }})</span>
            </div>
            <span class="flex items-center gap-1">
              🧡
              {{ tripDetails.likes }}
            </span>
            <span class="flex items-center gap-1">
              <share-icon class="h-3 w-3" />
              {{ tripDetails.shares }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="py-8 px-6 max-w-7xl mx-auto">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-2">
          <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
            <h2 class="text-2xl font-bold mb-2 md:mb-0">여행 일정</h2>

            <div class="flex gap-3">
              <button
                @click="toggleLike"
                :class="[
                  'px-4 py-2 border rounded-md transition-colors flex items-center gap-2',
                  isLiked
                    ? 'bg-red-50 border-red-300 text-red-600'
                    : 'hover:bg-gray-50 border-gray-300',
                ]"
                :disabled="isLikeLoading"
              >
                <heart-icon :class="['h-4 w-4', isLiked ? 'fill-current text-red-500' : '']" />
                {{ isLiked ? '좋아요 취소' : '좋아요' }} ({{ likeCount }})
              </button>

              <button
                v-if="userIds.includes(user.id)"
                @click="goToEdit"
                class="px-4 py-2 border border-blue-300 rounded-md transition-colors flex items-center gap-2 hover:bg-blue-50 text-blue-600"
              >
                수정하기
              </button>
            </div>
          </div>

          <div class="mb-8">
            <p class="text-gray-700">{{ tripDetails.description }}</p>
          </div>

          <div class="border-b mb-6">
            <div class="flex">
              <button
                v-for="(day, index) in tripDetails.itinerary"
                :key="index"
                :class="[
                  'px-4 py-2 text-center',
                  activeDay === index
                    ? 'border-b-2 border-blue-600 text-blue-600'
                    : 'text-gray-500',
                ]"
                @click="activeDay = index"
              >
                Day {{ index + 1 }}
              </button>
            </div>
          </div>

          <div
            v-for="(day, dayIndex) in tripDetails.itinerary"
            :key="dayIndex"
            v-show="activeDay === dayIndex"
          >
            <div class="bg-white rounded-lg border shadow-sm overflow-hidden">
              <div class="p-4 border-b bg-gray-50">
                <h3 class="text-lg font-medium">Day {{ dayIndex + 1 }}:</h3>
              </div>
              <div class="p-4">
                <div class="space-y-6">
                  <div v-for="(item, itemIndex) in day.items" :key="itemIndex" class="flex gap-4">
                    <div class="w-16 text-sm font-medium text-gray-500">{{ item.time }}</div>
                    <div class="relative">
                      <div
                        class="w-3 h-3 rounded-full bg-blue-500 absolute left-0 top-2.5 -translate-x-[7px]"
                      ></div>
                      <div
                        class="border-l-2 border-gray-200 pl-4 ml-0.5 pb-6 last:border-l-0 last:pb-0"
                      >
                        <h4 class="font-medium">{{ item.title }}</h4>
                        <p class="text-sm text-gray-500 mt-1">{{ item.description }}</p>
                        <span
                          class="mt-2 px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded-full inline-block"
                        >
                          {{ item.type }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="mt-8">
            <h3 class="text-xl font-medium mb-4">지도 보기</h3>
            <div class="h-[400px] border rounded-lg">
              <TripPlannerMap
                v-if="processedItinerary.length > 0"
                :locations="[]"
                :itinerary="processedItinerary"
                :active-day="activeDay"
              />
            </div>
          </div>

          <!-- 댓글 섹션 -->
          <div class="mt-8">
            <h3 class="text-xl font-medium mb-4">댓글 ({{ comments.length }})</h3>
            
            <!-- 댓글 작성 폼 -->
            <div class="bg-white rounded-lg border shadow-sm overflow-hidden mb-6">
              <div class="p-6">
                <div v-if="user && user.id">
                  <div class="flex items-start gap-3">
                    <div class="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center flex-shrink-0">
                      <span class="text-blue-600 font-medium">{{ user.name ? user.name.charAt(0) : 'U' }}</span>
                    </div>
                    <div class="flex-1">
                      <textarea
                        v-model="newComment"
                        placeholder="이 여행 계획에 대한 의견을 남겨주세요..."
                        class="w-full p-3 border border-gray-300 rounded-lg resize-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        rows="3"
                        :disabled="isCommentSubmitting"
                      ></textarea>
                      <div class="flex justify-between items-center mt-3">
                        <span class="text-sm text-gray-500">{{ newComment.length }}/500</span>
                        <button
                          @click="submitComment"
                          :disabled="!newComment.trim() || newComment.length > 500 || isCommentSubmitting"
                          class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                        >
                          {{ isCommentSubmitting ? '작성 중...' : '댓글 작성' }}
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-else class="text-center py-4">
                  <p class="text-gray-500 mb-3">댓글을 작성하려면 로그인이 필요합니다.</p>
                  <button
                    @click="$router.push('/login')"
                    class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                  >
                    로그인하기
                  </button>
                </div>
              </div>
            </div>

            <!-- 댓글 목록 -->
            <div class="space-y-4">
              <div v-if="isCommentsLoading" class="text-center py-8">
                <div class="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                <p class="mt-2 text-gray-500">댓글을 불러오는 중...</p>
              </div>
              
              <div v-else-if="comments.length === 0" class="text-center py-8">
                <p class="text-gray-500">아직 댓글이 없습니다.</p>
                <p class="text-sm text-gray-400 mt-1">첫 번째 댓글을 작성해보세요!</p>
              </div>

              <div
                v-else
                v-for="comment in comments"
                :key="comment.id"
                class="bg-white rounded-lg border shadow-sm overflow-hidden"
              >
                <div class="p-4">
                  <div class="flex items-start gap-3">
                    <div class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center flex-shrink-0">
                      <span class="text-gray-600 font-medium">
                        {{ comment.authorId ? comment.authorId : 'U' }}
                      </span>
                    </div>
                    <div class="flex-1">
                      <div class="flex items-center gap-2 mb-2">
                        <span class="font-medium text-gray-900">{{ comment.authorName || '익명' }}</span>
                        <span class="text-sm text-gray-500">{{ formatDate(comment.createdAt) }}</span>
                        <span v-if="comment.createdAt !== comment.updatedAt" class="text-xs text-gray-400">
                          (수정됨)
                        </span>
                      </div>
                      
                      <!-- 댓글 내용 표시/수정 -->
                      <div v-if="editingCommentId !== comment.id">
                        <p class="text-gray-700 whitespace-pre-wrap">{{ comment.content }}</p>
                        
                        <!-- 댓글 작성자만 수정/삭제 버튼 표시 -->
                        <div v-if="user && user.id === comment.userId" class="flex gap-2 mt-3">
                          <button
                            @click="startEditComment(comment)"
                            class="text-sm text-blue-600 hover:text-blue-800 transition-colors"
                          >
                            수정
                          </button>
                          <button
                            @click="deleteComment(comment.id)"
                            class="text-sm text-red-600 hover:text-red-800 transition-colors"
                            :disabled="isDeletingComment"
                          >
                            {{ isDeletingComment ? '삭제 중...' : '삭제' }}
                          </button>
                        </div>
                      </div>
                      
                      <!-- 댓글 수정 폼 -->
                      <div v-else>
                        <textarea
                          v-model="editCommentContent"
                          class="w-full p-3 border border-gray-300 rounded-lg resize-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          rows="3"
                          :disabled="isCommentUpdating"
                        ></textarea>
                        <div class="flex justify-between items-center mt-3">
                          <span class="text-sm text-gray-500">{{ editCommentContent.length }}/500</span>
                          <div class="flex gap-2">
                            <button
                              @click="cancelEditComment"
                              class="px-3 py-1 text-sm text-gray-600 hover:text-gray-800 transition-colors"
                              :disabled="isCommentUpdating"
                            >
                              취소
                            </button>
                            <button
                              @click="updateComment(comment.id)"
                              :disabled="!editCommentContent.trim() || editCommentContent.length > 500 || isCommentUpdating"
                              class="px-3 py-1 text-sm bg-blue-600 text-white rounded hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                            >
                              {{ isCommentUpdating ? '수정 중...' : '수정 완료' }}
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="lg:col-span-1">
          <div class="bg-white rounded-lg border shadow-sm sticky top-24">
            <div class="p-4 border-b">
              <h3 class="text-lg font-medium">여행 정보</h3>
            </div>
            <div class="p-4 space-y-4">
              <div>
                <h4 class="text-sm text-gray-500 mb-1">작성자 성격</h4>
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="(tag, index) in authorPersonalityTags"
                    :key="index"
                    class="px-2 py-1 bg-purple-50 text-purple-600 text-xs rounded-full"
                  >
                    {{ tag }}
                  </span>
                  <span v-if="authorPersonalityTags.length === 0" class="text-xs text-gray-400">
                    성격 태그 없음
                  </span>
                </div>
              </div>

              <div>
                <h4 class="text-sm text-gray-500 mb-1">여행 스타일</h4>
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="(tag, index) in tripDetails.travelTags"
                    :key="index"
                    class="px-2 py-1 bg-blue-50 text-blue-600 text-xs rounded-full"
                  >
                    {{ tag }}
                  </span>
                </div>
              </div>

              <div class="pt-4 border-t">
                <button
                  class="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center justify-center"
                  @click="copyToMyPlanner"
                  :disabled="isCopyLoading"
                >
                  {{ isCopyLoading ? '처리 중...' : '내 플래너에 가져오기' }}
                  <arrow-right-icon class="ml-2 h-4 w-4" />
                </button>
              </div>

              <div class="pt-4 border-t">
                <h4 class="text-sm text-gray-500 mb-2">이 작성자의 다른 여행 계획</h4>
                <ul class="space-y-3">
                  <li v-for="trip in authorOtherTrips" :key="trip.id">
                    <router-link
                      :to="`/tripdetail/${trip.id}`"
                      class="text-sm text-blue-600 hover:underline block"
                    >
                      {{ trip.title }}
                    </router-link>
                  </li>
                  <li v-if="authorOtherTrips.length === 0">
                    <span class="text-sm text-gray-400">다른 여행 계획이 없습니다.</span>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, watch, onUpdated } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ArrowRight as ArrowRightIcon,
  Heart as HeartIcon,
  Share as ShareIcon,
} from 'lucide-vue-next'
import { useUserStore } from '@/store/user'
import TripPlannerMap from '@/components/trip/TripPlannerMap.vue'
import api from '@/api'

const userStore = useUserStore()
const user = computed(() => userStore.loginUser)
const route = useRoute()
const router = useRouter()
const activeDay = ref(0)

// 기존 상태들
const isLiked = ref(false)
const likeCount = ref(0)
const isLikeLoading = ref(false)
const isCopyLoading = ref(false)

const authorInfo = ref({
  name: '',
  userId: '',
})
const authorPersonalityTags = ref([])
const authorOtherTrips = ref([])

// 댓글 관련 상태
const comments = ref([])
const newComment = ref('')
const isCommentsLoading = ref(false)
const isCommentSubmitting = ref(false)
const editingCommentId = ref(null)
const editCommentContent = ref('')
const isCommentUpdating = ref(false)
const isDeletingComment = ref(false)

const tripDetails = ref({
  id: route.params.id || 'id',
  title: '제목',
  location: '지역',
  duration: 0,
  userId: '유저ID',
  image:
    'https://images.unsplash.com/photo-1601621915196-2ad9b06857b3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80',
  personalityTags: [],
  travelTags: [],
  likes: 0,
  shares: 0,
  description: '여행 계획 설명',
  itinerary: [],
})

const itinerary = ref([])
const processedItinerary = ref([])
const isKakaoMapsLoaded = ref(false)

// 댓글 관련 함수들
const fetchComments = async () => {
  isCommentsLoading.value = true
  try {
    const response = await api.get(`/api/v1/comment/plan/${route.params.id}`)
    const comment_res = response.data || []
    const enrichedComment = await Promise.all(
      comment_res.map(async (comment) => {
        const userResponse = await api.get(`/api/v1/plan/user-info/${comment.userId}`)
        return {
          ...comment,
          authorName: userResponse.data?.name || 'Unknown',
          authorId: userResponse.data?.userId || 'unknown',
        }
      })
    )

    comments.value = enrichedComment
  } catch (error) {
    console.error('댓글 조회 실패:', error)
    comments.value = []
  } finally {
    isCommentsLoading.value = false
  }
}
const submitComment = async () => {
  if (!user.value || !user.value.id) {
    alert('로그인이 필요합니다.')
    return
  }

  if (!newComment.value.trim()) {
    alert('댓글 내용을 입력해주세요.')
    return
  }

  if (newComment.value.length > 500) {
    alert('댓글은 500자 이내로 작성해주세요.')
    return
  }

  isCommentSubmitting.value = true
  try {
    const response = await api.post('/api/v1/comment', {
      planId: parseInt(route.params.id),
      userId: user.value.id,
      content: newComment.value.trim()
    })

    if (response.data) {
      // 새 댓글을 목록 맨 앞에 추가
      const newCommentData = {
        id: response.data.id,
        content: newComment.value.trim(),
        userId: user.value.id,
        userName: user.value.name,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
      comments.value.unshift(newCommentData)
      newComment.value = ''
    }
    fetchComments()
  } catch (error) {
    console.error('댓글 작성 실패:', error)
    alert('댓글 작성에 실패했습니다.')
  } finally {
    isCommentSubmitting.value = false
  }
}

const startEditComment = (comment) => {
  editingCommentId.value = comment.id
  editCommentContent.value = comment.content
}

const cancelEditComment = () => {
  editingCommentId.value = null
  editCommentContent.value = ''
}

const updateComment = async (commentId) => {
  if (!editCommentContent.value.trim()) {
    alert('댓글 내용을 입력해주세요.')
    return
  }

  if (editCommentContent.value.length > 500) {
    alert('댓글은 500자 이내로 작성해주세요.')
    return
  }

  isCommentUpdating.value = true
  try {
    const response = await api.put(`/api/v1/comment/${commentId}`, {
      content: editCommentContent.value.trim()
    })

    if (response.data) {
      // 댓글 목록에서 해당 댓글 업데이트
      const commentIndex = comments.value.findIndex(c => c.id === commentId)
      if (commentIndex !== -1) {
        comments.value[commentIndex].content = editCommentContent.value.trim()
        comments.value[commentIndex].updatedAt = new Date().toISOString()
      }
      cancelEditComment()
    }
  } catch (error) {
    console.error('댓글 수정 실패:', error)
    alert('댓글 수정에 실패했습니다.')
  } finally {
    isCommentUpdating.value = false
  }
}

const deleteComment = async (commentId) => {
  if (!confirm('댓글을 삭제하시겠습니까?')) {
    return
  }

  isDeletingComment.value = true
  try {
    await api.delete(`/api/v1/comment/${commentId}`)
    
    // 댓글 목록에서 제거
    comments.value = comments.value.filter(c => c.id !== commentId)
  } catch (error) {
    console.error('댓글 삭제 실패:', error)
    alert('댓글 삭제에 실패했습니다.')
  } finally {
    isDeletingComment.value = false
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diffInMinutes = Math.floor((now - date) / (1000 * 60))
  
  if (diffInMinutes < 1) return '방금 전'
  if (diffInMinutes < 60) return `${diffInMinutes}분 전`
  
  const diffInHours = Math.floor(diffInMinutes / 60)
  if (diffInHours < 24) return `${diffInHours}시간 전`
  
  const diffInDays = Math.floor(diffInHours / 24)
  if (diffInDays < 7) return `${diffInDays}일 전`
  
  return date.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 기존 함수들 (변경 없음)
const toggleLike = async () => {
  if (!user.value || !user.value.id) {
    alert('로그인이 필요합니다.')
    return
  }

  isLikeLoading.value = true
  try {
    const response = await api.post(
      `/api/v1/likes/toggle/${route.params.id}?userId=${user.value.id}`,
    )

    if (response.data.status === 'SUCCESS') {
      isLiked.value = response.data.isLiked
      likeCount.value = response.data.likeCount
      tripDetails.value.likes = response.data.likeCount
    }
  } catch (error) {
    console.error('좋아요 처리 실패:', error)
    alert('좋아요 처리에 실패했습니다.')
  } finally {
    isLikeLoading.value = false
  }
}

const checkLikeStatus = async () => {
  if (!user.value || !user.value.id) return

  try {
    const response = await api.get(`/api/v1/likes/check/${route.params.id}?userId=${user.value.id}`)
    isLiked.value = response.data.isLiked
    likeCount.value = response.data.likeCount
  } catch (error) {
    console.error('좋아요 상태 확인 실패:', error)
  }
}

const processItineraryData = () => {
  processedItinerary.value = itinerary.value.map((day) => {
    return {
      day: day.day,
      title: `Day ${day.day + 1}`,
      items: day.items.map((item) => {
        if (item.items && typeof item.items === 'object') {
          return {
            title: item.items.title,
            time: item.items.time || '',
            description: item.items.memo || '',
            type: item.items.type,
            placeData: item.items.placeData,
          }
        }
        return item
      }),
    }
  })
}

const checkKakaoMapsLoaded = () => {
  if (!window.kakao || !window.kakao.maps) {
    console.warn("Kakao Maps API is not loaded. Make sure it's included in your index.html")
    isKakaoMapsLoaded.value = false
    return false
  }
  isKakaoMapsLoaded.value = true
  return true
}

const fetchAuthorInfo = async (authorId) => {
  try {
    const userResponse = await api.get(`/api/v1/plan/user-info/${authorId}`)
    if (userResponse.data) {
      authorInfo.value = {
        name: userResponse.data.name,
        userId: userResponse.data.userId,
      }
    }

    const personalityResponse = await api.get(`/api/v1/tag/user/personal/${authorId}`)
    authorPersonalityTags.value = personalityResponse.data || []

    const tripsResponse = await api.get(`/api/v1/plan/user/${authorId}`)
    const allTrips = tripsResponse.data || []
    authorOtherTrips.value = allTrips
      .filter((trip) => trip.id !== parseInt(route.params.id))
      .slice(0, 3)
  } catch (error) {
    console.error('작성자 정보 가져오기 실패:', error)
  }
}

const copyToMyPlanner = async () => {
  if (!user.value || !user.value.id) {
    alert('로그인이 필요합니다.')
    return
  }

  isCopyLoading.value = true

  try {
    const shareResponse = await api.patch(`/api/v1/plan/updateShare/${route.params.id}`)
    if (shareResponse.data && shareResponse.data.shares !== undefined) {
      tripDetails.value.shares = shareResponse.data.shares
    } else {
      tripDetails.value.shares += 1
    }

    const convertedItinerary = itinerary.value.map((day) => ({
      day: day.day,
      items: day.items.map((item) => ({
        title: item.title,
        type: item.type || '관광',
        memo: item.description || item.memo || '',
        time: item.time || '',
        placeData: {
          no: item.placeData?.no || item.placeData?.attraction_id || item.placeData?.content_id,
          latitude: item.placeData?.latitude || 0,
          longitude: item.placeData?.longitude || 0,
          contentTypeId: item.placeData?.contentTypeId || 12,
          title: item.title,
          addr: item.placeData?.addr || '',
          image: item.placeData?.image || '/placeholder.svg?height=150&width=150',
        },
      })),
    }))

    const planData = {
      destination: tripDetails.value.location,
      duration: tripDetails.value.duration,
      members: 1,
      transport: 'car',
      title: `${tripDetails.value.title} (복사본)`,
      description: tripDetails.value.description,
      itinerary: JSON.stringify(convertedItinerary),
      tags: JSON.stringify(tripDetails.value.travelTags),
    }

    const queryString = new URLSearchParams(planData).toString()
    router.push(`/tripplan?${queryString}`)
  } catch (error) {
    console.error('공유 수 업데이트 실패:', error)
    alert('공유 처리에 실패했습니다.')
  } finally {
    isCopyLoading.value = false
  }
}

const chatroomId = ref(0)
const userIds = ref([])

const loadTripData = async (tripId) => {
  try {
    const response = await api.get(`/api/v1/plan/${tripId}`)

    tripDetails.value.title = response.data.title
    tripDetails.value.duration = response.data.duration
    tripDetails.value.userId = response.data.userId
    tripDetails.value.description = response.data.description
    tripDetails.value.likes = response.data.likes
    tripDetails.value.shares = response.data.shares
    tripDetails.value.location = response.data.destination
    tripDetails.value.image = response.data.image

    chatroomId.value = response.data.chatroomId

    const resp_userIds = await api.get(`/api/v1/chat/user/${chatroomId.value}`)
    userIds.value = resp_userIds.data

    await fetchAuthorInfo(response.data.userId)
    await checkLikeStatus()

    itinerary.value = []
    for (let i = 0; i < response.data.duration; i++) {
      itinerary.value.push({ day: i, items: [] })
    }

    const tagRes = await api.get(`/api/v1/tag/plan/${tripId}`)
    tripDetails.value.travelTags = tagRes.data

    const itineraryRes = await api.get(`/api/v1/plan/itinerary/${tripId}`)
    const typeMap = {
      12: '관광',
      39: '식당',
      32: '숙박',
    }

    const grouped = itineraryRes.data.reduce((acc, cur) => {
      const { dayNumber } = cur
      if (!acc[dayNumber]) acc[dayNumber] = []

      acc[dayNumber].push({
        time: '',
        title: cur.title,
        description: cur.memo || '',
        type: typeMap[cur.contentTypeId] || '기타',
      })

      return acc
    }, {})

    tripDetails.value.itinerary = Object.entries(grouped).map(([day, items]) => ({
      day: Number(day),
      title: `Day ${day}`,
      items,
    }))

    itineraryRes.data.forEach((res) => {
      const dayIndex = res.dayNumber - 1
      if (dayIndex >= 0 && dayIndex < itinerary.value.length) {
        itinerary.value[dayIndex].items.push({
          title: res.title,
          time: '',
          description: res.memo || '',
          type: typeMap[res.contentTypeId] || '기타',
          placeData: {
            no: res.attractionId,
            latitude: res.latitude,
            longitude: res.longitude,
            contentTypeId: res.contentTypeId,
            title: res.title,
            addr: res.addr,
            image: res.image,
          },
        })
      }
    })

    processItineraryData()
    checkKakaoMapsLoaded()
  } catch (error) {
    console.error('Failed to fetch trip details:', error)
  }
}

const goToEdit = () => {
  router.push(`/tripplanupdate/${route.params.id}`)
}

onMounted(async () => {
  await loadTripData(route.params.id)
  await fetchSidos()
  await fetchComments() // 댓글 로드 추가
})


watch(
  () => route.params.id,
  async (newId, oldId) => {
    if (newId && newId !== oldId) {
      tripDetails.value = {
        id: newId,
        title: '제목',
        location: '지역',
        duration: 0,
        userId: '유저ID',
        image:
          'https://images.unsplash.com/photo-1601621915196-2ad9b06857b3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1200&q=80',
        personalityTags: [],
        travelTags: [],
        likes: 0,
        shares: 0,
        description: '여행 계획 설명',
        itinerary: [],
      }

      authorInfo.value = { name: '', userId: '' }
      authorPersonalityTags.value = []
      authorOtherTrips.value = []
      itinerary.value = []
      processedItinerary.value = []
      activeDay.value = 0
      comments.value = [] // 댓글 초기화 추가

      await loadTripData(newId)
      await fetchComments() // 새 댓글 로드 추가
    }
  },
  { immediate: false },
)

watch(
  [() => itinerary.value, () => activeDay.value],
  () => {
    processItineraryData()
  },
  { deep: true },
)

const sidos = ref([])
const fetchSidos = async () => {
  try {
    const response = await api.get('/api/v1/sidos')
    sidos.value = response.data
  } catch (error) {
    console.error('시도 목록 조회 실패:', error)
  }
}

const getSidoName = (sidoCode) => {
  if (!sidoCode || !sidos.value.length) return '지역 정보 없음'

  const sido = sidos.value.find((sido) => sido.sidoCode === sidoCode)
  return sido ? sido.sidoName : '지역 정보 없음'
}
</script>

<style scoped>
/* 추가 스타일이 필요한 경우 여기에 작성 */
</style>
