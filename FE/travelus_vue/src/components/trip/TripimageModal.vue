<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] flex flex-col">
      <div class="p-4 border-b flex justify-between items-center">
        <h2 class="text-xl font-bold">대표 이미지 선택</h2>
        <button @click="close" class="text-gray-500 hover:text-gray-700">
          <x-icon class="h-5 w-5" />
        </button>
      </div>
      
      <div class="p-4 flex-1 overflow-y-auto">
        <!-- 로딩 상태 -->
        <div v-if="loading" class="flex flex-col items-center justify-center py-10">
          <loader-icon class="h-10 w-10 text-blue-500 animate-spin mb-4" />
          <p class="text-gray-600">AI가 여행 일정에 맞는 이미지를 추천하고 있습니다...</p>
        </div>
        
        <!-- 에러 상태 -->
        <div v-else-if="error" class="text-center py-10">
          <alert-circle-icon class="h-10 w-10 text-red-500 mx-auto mb-4" />
          <p class="text-red-500 mb-2">{{ error }}</p>
          <button @click="loadRecommendedImages" class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">
            다시 시도
          </button>
        </div>
        
        <!-- 추천 이미지 -->
        <div v-else-if="recommendedImages.length > 0" class="mb-6">
          <h3 class="text-lg font-medium mb-3">AI 추천 이미지</h3>
          <p class="text-sm text-gray-500 mb-4">여행 일정에 맞게 AI가 추천한 이미지입니다. 원하는 이미지를 선택하세요.</p>
          
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            <div 
              v-for="(image, index) in recommendedImages" 
              :key="`rec-${index}`"
              class="border rounded-lg overflow-hidden cursor-pointer hover:shadow-md transition-shadow"
              :class="{'ring-2 ring-blue-500': selectedImage && selectedImage.type === 'ai' && selectedImage.index === index}"
              @click="selectImage('ai', index)"
            >
              <div class="aspect-video bg-gray-100 relative">
                <img :src="image.url" :alt="image.description" class="w-full h-full object-cover" />
                <div class="absolute top-2 right-2 bg-blue-500 text-white text-xs px-2 py-1 rounded-full">
                  AI 추천
                </div>
              </div>
              <div class="p-3">
                <p class="text-sm text-gray-700 line-clamp-2">{{ image.description }}</p>
                <div class="mt-2 flex flex-wrap gap-1">
                  <span 
                    v-for="(keyword, kidx) in image.keywords" 
                    :key="`kw-${index}-${kidx}`" 
                    class="text-xs bg-gray-100 text-gray-600 px-2 py-0.5 rounded-full"
                  >
                    {{ keyword }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 사용자 이미지 업로드 -->
        <div>
          <h3 class="text-lg font-medium mb-3">내 이미지 업로드</h3>
          <div class="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center">
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
              class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
            >
              파일 선택
            </button>
          </div>
          
          <!-- 업로드된 이미지 미리보기 -->
          <div v-if="uploadedImage" class="mt-4 border rounded-lg overflow-hidden">
            <div class="aspect-video bg-gray-100 relative">
              <img :src="uploadedImage.url" alt="업로드된 이미지" class="w-full h-full object-cover" />
            </div>
            <div class="p-3 flex justify-between items-center">
              <p class="text-sm text-gray-700 truncate">{{ uploadedImage.name }}</p>
              <div class="flex gap-2">
                <button 
                  @click="removeUploadedImage" 
                  class="p-1 text-gray-400 hover:text-red-500 rounded-full hover:bg-gray-100"
                >
                  <trash-icon class="h-4 w-4" />
                </button>
                <button 
                  @click="selectImage('upload', 0)" 
                  class="px-3 py-1 text-sm bg-blue-500 text-white rounded-md hover:bg-blue-600"
                  :class="{'bg-blue-700': selectedImage && selectedImage.type === 'upload'}"
                >
                  선택
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="p-4 border-t flex justify-end gap-3">
        <button @click="close" class="px-4 py-2 border rounded-md hover:bg-gray-50">
          취소
        </button>
        <button 
          @click="save" 
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          :disabled="!selectedImage"
          :class="{'opacity-50 cursor-not-allowed': !selectedImage}"
        >
          선택 완료
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { 
  X as XIcon, 
  Upload as UploadIcon, 
  Trash as TrashIcon,
  Loader as LoaderIcon,
  AlertCircle as AlertCircleIcon
} from 'lucide-vue-next'
import api from '@/api'

const props = defineProps({
  isOpen: Boolean,
  itinerary: {
    type: Array,
    default: () => []
  },
  title: {
    type: String,
    default: ''
  },
  description: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['close', 'save'])

// 상태 관리
const loading = ref(false)
const error = ref(null)
const recommendedImages = ref([])
const uploadedImage = ref(null)
const selectedImage = ref(null)
const fileInput = ref(null)

// 모달이 열릴 때 AI 추천 이미지 로드
watch(() => props.isOpen, (isOpen) => {
  if (isOpen) {
    loadRecommendedImages()
  }
})

// AI 추천 이미지 로드
const loadRecommendedImages = async () => {
  loading.value = true
  error.value = null
  recommendedImages.value = []
  
  try {
    // AI에 이미지 추천 요청
    const response = await api.post('/ai/recommend-images', {
      title: props.title,
      description: props.description,
      itinerary: props.itinerary
    })
    
    if (response.data.status === 'SUCCESS') {
      if (response.data.recommendations && response.data.recommendations.images) {
        // 추천 이미지 처리
        recommendedImages.value = response.data.recommendations.images.map((img, index) => ({
          url: getImageUrlFromKeywords(img.keywords || []),
          description: img.description,
          keywords: img.keywords || []
        }))
      } else if (response.data.rawResponse) {
        // 원본 텍스트 응답 처리
        error.value = '이미지 추천을 처리할 수 없습니다. 다시 시도해주세요.'
      }
    } else {
      error.value = response.data.message || '이미지 추천 중 오류가 발생했습니다.'
    }
  } catch (err) {
    console.error('이미지 추천 오류:', err)
    error.value = '이미지 추천 중 오류가 발생했습니다. 다시 시도해주세요.'
  } finally {
    loading.value = false
  }
}

// 키워드를 기반으로 Unsplash 이미지 URL 생성
const getImageUrlFromKeywords = (keywords) => {
  const keyword = keywords.length > 0 ? encodeURIComponent(keywords.join(',')) : 'travel'
  const width = 600
  const height = 400
  return `https://source.unsplash.com/random/${width}x${height}?${keyword}`
}

// 파일 업로드 처리
const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  // 파일 크기 검사 (5MB 제한)
  if (file.size > 5 * 1024 * 1024) {
    alert('파일 크기는 5MB를 초과할 수 없습니다.')
    return
  }
  
  // 이미지 미리보기 생성
  const reader = new FileReader()
  reader.onload = (e) => {
    uploadedImage.value = {
      file,
      name: file.name,
      url: e.target.result
    }
    // 업로드된 이미지 자동 선택
    selectImage('upload', 0)
  }
  reader.readAsDataURL(file)
}

// 업로드된 이미지 제거
const removeUploadedImage = () => {
  uploadedImage.value = null
  if (selectedImage.value && selectedImage.value.type === 'upload') {
    selectedImage.value = null
  }
  if (fileInput.value) {
    fileInput.value.value = ''
  }
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
  uploadedImage.value = null
  recommendedImages.value = []
  error.value = null
}

// 선택 완료
const save = () => {
  if (!selectedImage.value) return
  
  let selectedImageData = null
  
  if (selectedImage.value.type === 'ai') {
    const image = recommendedImages.value[selectedImage.value.index]
    selectedImageData = {
      img: image.url,
      description: image.description,
      keywords: image.keywords
    }
  } else if (selectedImage.value.type === 'upload' && uploadedImage.value) {
    selectedImageData = {
      img: uploadedImage.value.url,
      name: uploadedImage.value.name
    }
  }
  
  if (selectedImageData) {
    emit('save', selectedImageData)
    close()
  }
}
</script>
