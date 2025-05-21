<template>
  <div 
    ref="mapContainer" 
    class="bg-gray-100 rounded-lg overflow-hidden relative h-full" 
  >
    <div v-if="!mapLoaded" class="absolute inset-0 flex items-center justify-center">
      <div class="text-center">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mb-2"></div>
        <p class="text-gray-500">지도를 불러오는 중...</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'

const props = defineProps({
  locations: {
    type: Array,
    default: () => []
  },
  itinerary: {
    type: Array,
    default: () => []
  },
  activeDay: {
    type: Number,
    default: 0
  }
})

const mapContainer = ref(null)
const mapLoaded = ref(false)
let map = null
let markers = []
let polyline = null

// 지도 초기화
const initMap = () => {
  if (!window.kakao || !window.kakao.maps) {
    console.error('Kakao Maps API not loaded')
    return
  }

  const options = {
    center: new window.kakao.maps.LatLng(33.450701, 126.570667),
    level: 7
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

// 마커 및 경로 업데이트
const updateMapMarkers = () => {
  if (!map) return
  
  // 기존 마커 제거
  markers.forEach(marker => marker.setMap(null))
  markers = []
  
  // 기존 경로선 제거
  if (polyline) {
    polyline.setMap(null)
    polyline = null
  }
  
  // 현재 활성화된 일차의 장소들
  const items = props.itinerary[props.activeDay]?.items || []
  const places = items
    .filter(item => item.placeData && item.placeData.longitude && item.placeData.latitude)
    .map(item => ({
      title: item.title,
      latlng: new window.kakao.maps.LatLng(
        parseFloat(item.placeData.latitude), 
        parseFloat(item.placeData.longitude)
      ),
      image: item.image,
      type: item.placeData.contentTypeId==12?'명소':(item.placeData.contentTypeId==32?'숙소':'식당'),
    }))
  
  if (places.length === 0) {
    // 장소가 없으면 기본 위치로 이동
    map.setCenter(new window.kakao.maps.LatLng(33.450701, 126.570667))
    return
  }
  
  // 마커 이미지 설정
  const createMarkerImage = (index) => {
    return new window.kakao.maps.MarkerImage(
      `https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png`,
      new window.kakao.maps.Size(36, 37),
      {
        offset: new window.kakao.maps.Point(13, 37),
        spriteSize: new window.kakao.maps.Size(36, 691),
        spriteOrigin: new window.kakao.maps.Point(0, (index * 46) + 10)
      }
    )
  }
  
  // 마커 생성
  places.forEach((place, index) => {
    const marker = new window.kakao.maps.Marker({
      position: place.latlng,
      map: map,
      title: place.title,
      image: createMarkerImage(index),
      zIndex: places.length - index
    })
    
    // 인포윈도우 생성
    const infoContent = `
      <div class="p-2 max-w-[200px]">
        <div class="font-bold text-sm mb-1">${index + 1}. ${place.title}</div>
        <div class="text-xs text-blue-600 bg-blue-50 px-1 py-0.5 rounded-full inline-block">${place.type}</div>
      </div>
    `
    
    const infoWindow = new window.kakao.maps.InfoWindow({
      content: infoContent,
      removable: true
    })
    
    // 마커 클릭 시 인포윈도우 표시
    window.kakao.maps.event.addListener(marker, 'click', function() {
      infoWindow.open(map, marker)
    })
    
    markers.push(marker)
  })
  
  // 경로선 그리기
  if (places.length > 1) {
    const linePath = places.map(place => place.latlng)
    
    polyline = new window.kakao.maps.Polyline({
      path: linePath,
      strokeWeight: 3,
      strokeColor: '#5882FA',
      strokeOpacity: 0.8,
      strokeStyle: 'solid'
    })
    
    polyline.setMap(map)
  }
  
  // 모든 마커가 보이도록 지도 범위 재설정
  if (places.length > 0) {
    const bounds = new window.kakao.maps.LatLngBounds()
    places.forEach(place => bounds.extend(place.latlng))
    map.setBounds(bounds)
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

// itinerary 또는 activeDay가 변경될 때 마커와 경로 업데이트
watch([() => props.itinerary, () => props.activeDay], () => {
  if (map) {
    updateMapMarkers()
  }
}, { deep: true })
</script>

<style scoped>
/* 인포윈도우 스타일 조정 */
:deep(.custom-infowindow) {
  padding: 8px;
  border-radius: 4px;
  max-width: 200px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

:deep(.custom-infowindow .title) {
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 4px;
}

:deep(.custom-infowindow .type) {
  font-size: 12px;
  color: #2563eb;
  background-color: #eff6ff;
  padding: 2px 6px;
  border-radius: 9999px;
  display: inline-block;
}
</style>