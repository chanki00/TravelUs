<template>
  <div
    v-if="isOpen"
    class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
  >
    <div class="bg-white rounded-lg shadow-xl w-full max-w-5xl max-h-[90vh] flex flex-col">
      <div class="p-4 border-b flex justify-between items-center">
        <h2 class="text-xl font-bold">대표 이미지 선택</h2>
        <button @click="close" class="text-gray-500 hover:text-gray-700">
          <x-icon class="h-5 w-5" />
        </button>
      </div>

      <div class="p-4 flex-1 overflow-y-auto">
        <!-- 추천 이미지 (고정) -->
        <div class="mb-6">
          <div class="flex items-center justify-between mb-4">
            <div>
              <h3 class="text-lg font-medium">추천 이미지</h3>
              <p class="text-sm text-gray-500">여행에 어울리는 아름다운 이미지들을 준비했습니다</p>
            </div>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            <div
              v-for="(image, index) in recommendedImages"
              :key="`rec-${index}`"
              class="border rounded-lg overflow-hidden cursor-pointer hover:shadow-lg transition-all duration-200"
              :class="{
                'ring-2 ring-blue-500 shadow-lg':
                  selectedImage &&
                  selectedImage.type === 'recommended' &&
                  selectedImage.index === index,
              }"
              @click="selectImage('recommended', index)"
            >
              <div class="aspect-video bg-gray-100 relative">
                <img :src="image.url" :alt="image.description" class="w-full h-full object-cover" />

                <!-- 추천 배지 -->
                <div
                  class="absolute top-2 right-2 bg-green-500 text-white text-xs px-2 py-1 rounded-full flex items-center gap-1"
                >
                  <heart-icon class="h-3 w-3" />
                  추천
                </div>

                <!-- 선택 표시 -->
                <div
                  v-if="
                    selectedImage &&
                    selectedImage.type === 'recommended' &&
                    selectedImage.index === index
                  "
                  class="absolute top-2 left-2"
                >
                  <div class="w-6 h-6 bg-blue-600 rounded-full flex items-center justify-center">
                    <check-icon class="h-4 w-4 text-white" />
                  </div>
                </div>
              </div>

              <div class="p-3">
                <p class="text-sm text-gray-700 line-clamp-2 mb-2">{{ image.description }}</p>

                <!-- 키워드 태그 -->
                <div class="flex flex-wrap gap-1 mb-2">
                  <span
                    v-for="(keyword, kidx) in image.keywords.slice(0, 3)"
                    :key="`kw-${index}-${kidx}`"
                    class="text-xs bg-green-50 text-green-600 px-2 py-0.5 rounded-full"
                  >
                    {{ keyword }}
                  </span>
                </div>

                <!-- 이미지 출처 -->
                <div class="flex items-center justify-between">
                  <span class="text-xs text-gray-500 flex items-center gap-1">
                    <external-link-icon class="h-3 w-3" />
                    {{ image.source }}
                  </span>
                  <span class="text-xs text-green-600 font-medium">✓ 무료 이미지</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 사용자 이미지 업로드 -->
        <div>
          <h3 class="text-lg font-medium mb-3">내 이미지 업로드</h3>

          <!-- 업로드 영역 -->
          <div
            class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center transition-colors"
            :class="{ 'border-blue-400 bg-blue-50': isDragOver }"
            @dragover.prevent="isDragOver = true"
            @dragleave.prevent="isDragOver = false"
            @drop.prevent="handleDrop"
          >
            <upload-icon class="h-10 w-10 text-gray-400 mx-auto mb-3" />
            <p class="text-gray-500 mb-2">이미지를 드래그하거나 클릭하여 업로드하세요</p>
            <p class="text-xs text-gray-400 mb-4">지원 형식: JPG, PNG, GIF (최대 5MB)</p>
            <input
              type="file"
              ref="fileInput"
              @change="handleFileUpload"
              accept="image/*"
              class="hidden"
            />
            <button
              @click="$refs.fileInput.click()"
              class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:opacity-50"
              :disabled="uploading"
            >
              <span v-if="uploading" class="flex items-center gap-2">
                <loader-icon class="h-4 w-4 animate-spin" />
                업로드 중...
              </span>
              <span v-else>파일 선택</span>
            </button>
          </div>

          <!-- 업로드된 이미지들 -->
          <div v-if="uploadedImages.length > 0" class="mt-4 space-y-3">
            <h4 class="text-sm font-medium text-gray-700">업로드된 이미지</h4>
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
              <div
                v-for="(image, index) in uploadedImages"
                :key="`upload-${index}`"
                class="border rounded-lg overflow-hidden cursor-pointer hover:shadow-md transition-all"
                :class="{
                  'ring-2 ring-blue-500 shadow-md':
                    selectedImage &&
                    selectedImage.type === 'upload' &&
                    selectedImage.index === index,
                }"
                @click="selectImage('upload', index)"
              >
                <div class="aspect-video bg-gray-100 relative">
                  <img :src="image.url" :alt="image.name" class="w-full h-full object-cover" />

                  <!-- 선택 표시 -->
                  <div
                    v-if="
                      selectedImage &&
                      selectedImage.type === 'upload' &&
                      selectedImage.index === index
                    "
                    class="absolute top-2 left-2"
                  >
                    <div class="w-6 h-6 bg-blue-600 rounded-full flex items-center justify-center">
                      <check-icon class="h-4 w-4 text-white" />
                    </div>
                  </div>

                  <!-- 삭제 버튼 -->
                  <button
                    @click.stop="removeUploadedImage(index)"
                    class="absolute top-2 right-2 w-6 h-6 bg-red-500 text-white rounded-full flex items-center justify-center hover:bg-red-600"
                  >
                    <x-icon class="h-3 w-3" />
                  </button>
                </div>

                <div class="p-2">
                  <p class="text-xs text-gray-600 truncate">{{ image.name }}</p>
                  <p class="text-xs text-gray-400">{{ formatFileSize(image.size) }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="p-4 border-t flex justify-end gap-3">
        <button @click="close" class="px-4 py-2 border rounded-md hover:bg-gray-50">취소</button>
        <button
          @click="save"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
          :disabled="!selectedImage"
        >
          선택 완료
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  X as XIcon,
  Upload as UploadIcon,
  Check as CheckIcon,
  Heart as HeartIcon,
  ExternalLink as ExternalLinkIcon,
  Loader as LoaderIcon,
} from 'lucide-vue-next'
import api from '@/api'

const props = defineProps({
  isOpen: Boolean,
  itinerary: {
    type: Array,
    default: () => [],
  },
  title: {
    type: String,
    default: '',
  },
  description: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['close', 'save'])

// 상태 관리
const selectedImage = ref(null)
const uploadedImages = ref([])
const uploading = ref(false)
const isDragOver = ref(false)
const fileInput = ref(null)

// 고정 추천 이미지
const recommendedImages = ref([
  {
    url: 'https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=800&h=600&fit=crop',
    description: '아름다운 산과 호수가 어우러진 자연 풍경',
    keywords: ['자연', '산', '호수'],
    source: 'Unsplash',
  },
  {
    url: 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop',
    description: '도시의 야경과 불빛이 아름다운 도심 풍경',
    keywords: ['도시', '야경', '불빛'],
    source: 'Unsplash',
  },
  {
    url: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&h=600&fit=crop',
    description: '푸른 바다와 해변이 펼쳐진 해안 풍경',
    keywords: ['바다', '해변', '휴양'],
    source: 'Unsplash',
  },
  {
    url: 'https://images.unsplash.com/photo-1513475382585-d06e58bcb0e0?w=800&h=600&fit=crop',
    description: '전통 건축물과 문화가 살아있는 역사적 장소',
    keywords: ['전통', '문화', '역사'],
    source: 'Unsplash',
  },
  {
    url: 'https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=800&h=600&fit=crop',
    description: '울창한 숲과 자연이 어우러진 힐링 공간',
    keywords: ['숲', '자연', '힐링'],
    source: 'Unsplash',
  },
])

// 드래그 앤 드롭 처리
const handleDrop = (event) => {
  isDragOver.value = false
  const files = event.dataTransfer.files
  if (files.length > 0) {
    handleFileUpload({ target: { files } })
  }
}

// 파일 업로드 처리
const handleFileUpload = async (event) => {
  const files = event.target.files
  if (!files || files.length === 0) return

  for (const file of files) {
    await uploadSingleFile(file)
  }

  // 파일 입력 초기화
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

// 단일 파일 업로드
const uploadSingleFile = async (file) => {
  // 파일 크기 검사 (5MB 제한)
  if (file.size > 5 * 1024 * 1024) {
    alert('파일 크기는 5MB를 초과할 수 없습니다.')
    return
  }

  // 파일 타입 검사
  if (!file.type.startsWith('image/')) {
    alert('이미지 파일만 업로드 가능합니다.')
    return
  }

  uploading.value = true

  try {
    const formData = new FormData()
    formData.append('file', file)

    const response = await api.post('/api/upload/image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    if (response.data.status === 'SUCCESS') {
      // 업로드된 이미지 목록에 추가
      const uploadedImage = {
        url: `http://INTERNAL_IP_REDACTED:8080${response.data.fileUrl}`, // 서버 URL 추가
        name: response.data.originalName,
        size: response.data.size,
        serverPath: response.data.fileUrl,
      }

      uploadedImages.value.push(uploadedImage)

      // 자동으로 마지막 업로드된 이미지 선택
      selectImage('upload', uploadedImages.value.length - 1)
    } else {
      alert(response.data.message || '파일 업로드에 실패했습니다.')
    }
  } catch (error) {
    console.error('파일 업로드 오류:', error)
    alert('파일 업로드 중 오류가 발생했습니다.')
  } finally {
    uploading.value = false
  }
}

// 업로드된 이미지 제거
const removeUploadedImage = (index) => {
  uploadedImages.value.splice(index, 1)

  // 선택된 이미지가 삭제된 경우 선택 해제
  if (selectedImage.value && selectedImage.value.type === 'upload') {
    if (selectedImage.value.index === index) {
      selectedImage.value = null
    } else if (selectedImage.value.index > index) {
      selectedImage.value.index--
    }
  }
}

// 파일 크기 포맷팅
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 이미지 선택
const selectImage = (type, index) => {
  selectedImage.value = { type, index }
}

// 모달 닫기
const close = () => {
  emit('close')
  // 상태 초기화
  selectedImage.value = null
  uploadedImages.value = []
  uploading.value = false
  isDragOver.value = false
}

// 선택 완료
const save = () => {
  if (!selectedImage.value) return

  let selectedImageData = null

  if (selectedImage.value.type === 'recommended') {
    const image = recommendedImages.value[selectedImage.value.index]
    selectedImageData = {
      img: image.url,
      description: image.description,
      keywords: image.keywords,
      source: image.source,
      type: 'recommended',
    }
  } else if (selectedImage.value.type === 'upload') {
    const image = uploadedImages.value[selectedImage.value.index]
    selectedImageData = {
      img: image.serverPath, // DB에는 서버 경로 저장
      name: image.name,
      type: 'upload',
    }
  }

  if (selectedImageData) {
    emit('save', selectedImageData)
    close()
  }
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
