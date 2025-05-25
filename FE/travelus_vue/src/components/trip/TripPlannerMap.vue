<template>
  <div ref="mapContainer" class="bg-gray-100 rounded-lg overflow-hidden relative h-full">
    <div v-if="!mapLoaded" class="absolute inset-0 flex items-center justify-center">
      <div class="text-center">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mb-2"></div>
        <p class="text-gray-500">지도를 불러오는 중...</p>
      </div>
    </div>

    <!-- 일차별 동선 컨트롤 -->
    <div
      v-if="mapLoaded"
      class="absolute top-4 left-4 bg-white rounded-lg shadow-lg p-3 z-10 max-w-xs"
    >
      <h4 class="text-sm font-bold mb-3 text-gray-700">일차별 동선 보기</h4>

      <!-- 전체 보기 버튼 -->
      <button
        @click="showAllDays"
        :class="[
          'w-full mb-2 px-3 py-2 text-xs rounded-md transition-all duration-200',
          selectedDay === 'all'
            ? 'bg-gray-800 text-white shadow-md'
            : 'bg-gray-100 text-gray-700 hover:bg-gray-200',
        ]"
      >
        <span class="flex items-center justify-center gap-2">
          <div class="w-2 h-2 rounded-full bg-gradient-to-r from-red-400 to-blue-400"></div>
          전체 일차
        </span>
      </button>

      <!-- 일차별 버튼들 -->
      <div class="space-y-1">
        <button
          v-for="(day, index) in itinerary"
          :key="index"
          @click="showSpecificDay(index)"
          :class="[
            'w-full px-3 py-2 text-xs rounded-md transition-all duration-200 flex items-center justify-between',
            selectedDay === index
              ? 'text-white shadow-md transform scale-105'
              : 'bg-gray-50 text-gray-700 hover:bg-gray-100 hover:shadow-sm',
          ]"
          :style="
            selectedDay === index ? { backgroundColor: dayColors[index % dayColors.length] } : {}
          "
        >
          <span class="flex items-center gap-2">
            <div
              class="w-3 h-3 rounded-full"
              :style="{ backgroundColor: dayColors[index % dayColors.length] }"
            ></div>
            {{ index + 1 }}일차
          </span>
          <span class="text-xs opacity-75"> {{ day.items.length }}곳 </span>
        </button>
      </div>

      <!-- 선택된 일차 정보 -->
      <div
        v-if="selectedDay !== 'all' && selectedDay !== null"
        class="mt-3 pt-3 border-t border-gray-200"
      >
        <div class="text-xs text-gray-600 mb-2">
          <strong>{{ selectedDay + 1 }}일차 일정</strong>
        </div>
        <div class="space-y-1 max-h-32 overflow-y-auto">
          <div
            v-for="(item, index) in itinerary[selectedDay]?.items || []"
            :key="index"
            class="text-xs p-2 bg-gray-50 rounded flex items-center gap-2"
          >
            <span class="font-medium text-gray-800 min-w-[16px]">{{ index + 1 }}.</span>
            <span class="text-gray-700 truncate">{{ item.title }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 범례 (우상단) -->
    <div
      v-if="mapLoaded && selectedDay === 'all'"
      class="absolute top-4 right-4 bg-white rounded-lg shadow-lg p-3 z-10"
    >
      <h4 class="text-sm font-bold mb-2">일차별 색상</h4>
      <div class="space-y-1">
        <div v-for="(day, index) in itinerary" :key="index" class="flex items-center gap-2 text-xs">
          <div
            class="w-3 h-3 rounded-full"
            :style="{ backgroundColor: dayColors[index % dayColors.length] }"
          ></div>
          <span>{{ index + 1 }}일차 ({{ day.items.length }}곳)</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onBeforeUnmount, computed } from 'vue'

const props = defineProps({
  locations: {
    type: Array,
    default: () => [],
  },
  itinerary: {
    type: Array,
    default: () => [],
  },
  activeDay: {
    type: Number,
    default: 0,
  },
  destination: {
    type: Number,
    default: null,
  },
})

const mapContainer = ref(null)
const mapLoaded = ref(false)
const selectedDay = ref('all') // 'all' 또는 일차 인덱스
let map = null
let markers = []
let polylines = []

// 일차별 색상 정의
const dayColors = [
  '#FF6B6B', // 1일차 - 빨간색
  '#4ECDC4', // 2일차 - 청록색
  '#45B7D1', // 3일차 - 파란색
  '#96CEB4', // 4일차 - 민트색
  '#FFEAA7', // 5일차 - 노란색
  '#DDA0DD', // 6일차 - 자주색
  '#98D8C8', // 7일차 - 연두색
  '#FFB6C1', // 8일차 - 분홍색
  '#87CEEB', // 9일차 - 하늘색
  '#F0E68C', // 10일차 - 카키색
]

// 시도별 중심 좌표
const sidoCenters = {
  1: { lat: 37.5665, lng: 126.978 }, // 서울
  2: { lat: 37.4563, lng: 126.7052 }, // 인천
  3: { lat: 37.2636, lng: 127.0286 }, // 대전
  4: { lat: 35.1796, lng: 129.0756 }, // 부산
  5: { lat: 35.8714, lng: 128.6014 }, // 대구
  6: { lat: 35.1595, lng: 126.8526 }, // 광주
  7: { lat: 36.5684, lng: 128.7294 }, // 울산
  8: { lat: 36.48, lng: 127.289 }, // 세종
  31: { lat: 37.4138, lng: 127.5183 }, // 경기
  32: { lat: 37.8228, lng: 128.1555 }, // 강원
  33: { lat: 36.5184, lng: 126.8 }, // 충북
  34: { lat: 36.5184, lng: 126.8 }, // 충남
  35: { lat: 35.7175, lng: 127.153 }, // 전북
  36: { lat: 34.8679, lng: 126.991 }, // 전남
  37: { lat: 36.4919, lng: 128.8889 }, // 경북
  38: { lat: 35.4606, lng: 128.2132 }, // 경남
  39: { lat: 33.4996, lng: 126.5312 }, // 제주
}

// 전체 일차 보기
const showAllDays = () => {
  selectedDay.value = 'all'
  updateMapMarkers()
}

// 특정 일차 보기
const showSpecificDay = (dayIndex) => {
  selectedDay.value = dayIndex
  updateMapMarkers()
}

// 지도 초기화
const initMap = () => {
  if (!window.kakao || !window.kakao.maps) {
    console.error('Kakao Maps API not loaded')
    return
  }

  // 초기 중심점 설정 (여행지 기준 또는 서울)
  const initialCenter = getInitialCenter()

  const options = {
    center: new window.kakao.maps.LatLng(initialCenter.lat, initialCenter.lng),
    level: 8,
  }

  map = new window.kakao.maps.Map(mapContainer.value, options)
  mapLoaded.value = true

  // 지도 컨트롤 추가
  const zoomControl = new window.kakao.maps.ZoomControl()
  map.addControl(zoomControl, window.kakao.maps.ControlPosition.RIGHT)

  const mapTypeControl = new window.kakao.maps.MapTypeControl()
  map.addControl(mapTypeControl, window.kakao.maps.ControlPosition.TOPRIGHT)

  // 초기 마커 및 경로 표시
  updateMapMarkers()
}

// 초기 중심점 계산
const getInitialCenter = () => {
  // 1. 일정표에 장소가 있으면 그 중심점 사용
  const allPlaces = getAllPlacesFromItinerary()
  if (allPlaces.length > 0) {
    const avgLat =
      allPlaces.reduce((sum, place) => sum + parseFloat(place.latitude), 0) / allPlaces.length
    const avgLng =
      allPlaces.reduce((sum, place) => sum + parseFloat(place.longitude), 0) / allPlaces.length
    return { lat: avgLat, lng: avgLng }
  }

  // 2. destination이 있으면 해당 시도 중심점 사용
  if (props.destination && sidoCenters[props.destination]) {
    return sidoCenters[props.destination]
  }

  // 3. 기본값은 서울
  return { lat: 37.5665, lng: 126.978 }
}

// 일정표의 모든 장소 가져오기
const getAllPlacesFromItinerary = () => {
  const places = []
  props.itinerary.forEach((day) => {
    day.items.forEach((item) => {
      if (item.placeData && item.placeData.longitude && item.placeData.latitude) {
        places.push({
          latitude: item.placeData.latitude,
          longitude: item.placeData.longitude,
          title: item.title,
          dayIndex: day.day,
        })
      }
    })
  })
  return places
}

// 마커 및 경로 업데이트
const updateMapMarkers = () => {
  if (!map) return

  // 기존 마커 제거
  markers.forEach((marker) => marker.setMap(null))
  markers = []

  // 기존 경로선 제거
  polylines.forEach((polyline) => polyline.setMap(null))
  polylines = []

  // 표시할 일차 결정
  const daysToShow =
    selectedDay.value === 'all' ? props.itinerary.map((_, index) => index) : [selectedDay.value]

  // 선택된 일차들의 장소들 처리
  daysToShow.forEach((dayIndex) => {
    const day = props.itinerary[dayIndex]
    if (!day) return

    const dayColor = dayColors[dayIndex % dayColors.length]
    const places = day.items
      .filter((item) => item.placeData && item.placeData.longitude && item.placeData.latitude)
      .map((item, itemIndex) => ({
        title: item.title,
        latlng: new window.kakao.maps.LatLng(
          parseFloat(item.placeData.latitude),
          parseFloat(item.placeData.longitude),
        ),
        image: item.image,
        type: getPlaceType(item.placeData.contentTypeId),
        dayIndex: dayIndex,
        itemIndex: itemIndex,
        color: dayColor,
      }))

    if (places.length === 0) return

    // 일차별 마커 생성
    places.forEach((place, index) => {
      const marker = new window.kakao.maps.Marker({
        position: place.latlng,
        map: map,
        title: place.title,
        image: createColoredMarkerImage(place.dayIndex + 1, place.color),
        zIndex: 100 - place.dayIndex,
      })

      // 인포윈도우 생성
      const infoContent = `
        <div class="p-3 max-w-[200px]">
          <div class="font-bold text-sm mb-1" style="color: ${place.color}">
            ${place.dayIndex + 1}일차 - ${index + 1}번째
          </div>
          <div class="font-medium text-sm mb-1">${place.title}</div>
          <div class="text-xs text-gray-600 bg-gray-100 px-2 py-1 rounded-full inline-block">${place.type}</div>
        </div>
      `

      const infoWindow = new window.kakao.maps.InfoWindow({
        content: infoContent,
        removable: true,
      })

      // 마커 클릭 시 인포윈도우 표시
      window.kakao.maps.event.addListener(marker, 'click', function () {
        // 다른 인포윈도우 닫기
        markers.forEach((m) => {
          if (m.infoWindow) {
            m.infoWindow.close()
          }
        })
        infoWindow.open(map, marker)
      })

      marker.infoWindow = infoWindow
      markers.push(marker)
    })

    // 일차별 경로선 그리기
    if (places.length > 1) {
      const linePath = places.map((place) => place.latlng)

      const polyline = new window.kakao.maps.Polyline({
        path: linePath,
        strokeWeight: selectedDay.value === 'all' ? 3 : 5, // 단일 일차 보기일 때 더 굵게
        strokeColor: dayColor,
        strokeOpacity: selectedDay.value === 'all' ? 0.7 : 0.9,
        strokeStyle: 'solid',
      })

      polyline.setMap(map)
      polylines.push(polyline)
    }
  })

  // 지도 범위 재설정
  const allPlacesToShow = []
  daysToShow.forEach((dayIndex) => {
    const day = props.itinerary[dayIndex]
    if (day) {
      day.items.forEach((item) => {
        if (item.placeData && item.placeData.longitude && item.placeData.latitude) {
          allPlacesToShow.push({
            latitude: item.placeData.latitude,
            longitude: item.placeData.longitude,
          })
        }
      })
    }
  })

  if (allPlacesToShow.length > 0) {
    const bounds = new window.kakao.maps.LatLngBounds()
    allPlacesToShow.forEach((place) => {
      bounds.extend(new window.kakao.maps.LatLng(place.latitude, place.longitude))
    })
    map.setBounds(bounds)

    // 줌 레벨이 너무 높으면 조정
    setTimeout(() => {
      if (map.getLevel() > 10) {
        map.setLevel(10)
      }
    }, 100)
  }
}

// 색상이 있는 마커 이미지 생성
const createColoredMarkerImage = (dayNumber, color) => {
  // SVG로 색상이 있는 마커 생성
  const svgMarker = `
    <svg width="30" height="40" viewBox="0 0 30 40" xmlns="http://www.w3.org/2000/svg">
      <path d="M15 0C6.716 0 0 6.716 0 15c0 8.284 15 25 15 25s15-16.716 15-25C30 6.716 23.284 0 15 0z" fill="${color}"/>
      <circle cx="15" cy="15" r="8" fill="white"/>
      <text x="15" y="20" text-anchor="middle" font-family="Arial, sans-serif" font-size="10" font-weight="bold" fill="${color}">${dayNumber}</text>
    </svg>
  `

  const svgBlob = new Blob([svgMarker], { type: 'image/svg+xml' })
  const svgUrl = URL.createObjectURL(svgBlob)

  return new window.kakao.maps.MarkerImage(svgUrl, new window.kakao.maps.Size(30, 40), {
    offset: new window.kakao.maps.Point(15, 40),
  })
}

// 장소 타입 반환
const getPlaceType = (contentTypeId) => {
  switch (contentTypeId) {
    case 12:
      return '관광지'
    case 14:
      return '문화시설'
    case 15:
      return '축제/공연/행사'
    case 25:
      return '여행코스'
    case 28:
      return '레포츠'
    case 32:
      return '숙소'
    case 38:
      return '쇼핑'
    case 39:
      return '식당'
    default:
      return '기타'
  }
}

// 지도 API 로드 확인 및 초기화
onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap()
  } else {
    // Kakao Maps API가 아직 로드되지 않은 경우 대기
    const checkKakaoMaps = setInterval(() => {
      if (window.kakao && window.kakao.maps) {
        clearInterval(checkKakaoMaps)
        initMap()
      }
    }, 100)

    // 10초 후에도 로드되지 않으면 타이머 중지
    setTimeout(() => {
      clearInterval(checkKakaoMaps)
      console.error('Kakao Maps API load timeout')
    }, 10000)
  }
})

// itinerary가 변경될 때 마커와 경로 업데이트
watch(
  () => props.itinerary,
  () => {
    if (map) {
      updateMapMarkers()
    }
  },
  { deep: true },
)

// destination이 변경될 때 지도 중심 이동
watch(
  () => props.destination,
  (newDestination) => {
    if (map && newDestination && sidoCenters[newDestination]) {
      const center = sidoCenters[newDestination]
      map.setCenter(new window.kakao.maps.LatLng(center.lat, center.lng))
    }
  },
)

onBeforeUnmount(() => {
  // 마커 이미지 URL 정리
  markers.forEach((marker) => {
    if (marker.getImage && marker.getImage().getSrc) {
      const src = marker.getImage().getSrc()
      if (src.startsWith('blob:')) {
        URL.revokeObjectURL(src)
      }
    }
  })
})
</script>

<style scoped>
/* 버튼 호버 효과 */
.transition-all {
  transition: all 0.2s ease-in-out;
}

/* 스크롤바 스타일링 */
:deep(.overflow-y-auto::-webkit-scrollbar) {
  width: 4px;
}

:deep(.overflow-y-auto::-webkit-scrollbar-track) {
  background: #f1f1f1;
  border-radius: 2px;
}

:deep(.overflow-y-auto::-webkit-scrollbar-thumb) {
  background: #c1c1c1;
  border-radius: 2px;
}

:deep(.overflow-y-auto::-webkit-scrollbar-thumb:hover) {
  background: #a8a8a8;
}
</style>
