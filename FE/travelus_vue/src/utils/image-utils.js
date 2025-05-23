// 이미지 URL 처리 유틸리티 함수

/**
 * 이미지 경로를 완전한 URL로 변환하는 함수
 * @param {string} imagePath - 이미지 경로
 * @param {string} defaultImage - 기본 이미지 경로 (선택사항)
 * @returns {string} 완전한 이미지 URL
 */
export const getImageUrl = (imagePath, defaultImage = '/images/default-trip.jpg') => {
  // 경로가 없는 경우 기본 이미지 반환
  if (!imagePath) {
    return defaultImage
  }

  // 이미 http나 https로 시작하는 경우 그대로 반환
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath
  }

  // 로컬 파일 시스템 절대 경로인 경우 (C:\Users\... 또는 /Users/...)
  if (imagePath.includes(':\\') || imagePath.includes('/Users/')) {
    // 파일명만 추출
    const fileName = imagePath.split(/[/\\]/).pop()
    // API 서버의 기본 URL을 가져와서 경로 앞에 붙임
    const baseUrl = import.meta.env.VITE_API_URL || window.location.origin
    return `${baseUrl}/uploads/images/${fileName}`
  }

  // 상대 경로인 경우 (예: /uploads/images/...)
  if (imagePath.startsWith('/')) {
    // API 서버의 기본 URL을 가져와서 경로 앞에 붙임
    const baseUrl = import.meta.env.VITE_API_URL || window.location.origin
    return `${baseUrl}${imagePath}`
  }

  // 파일명만 있는 경우 (예: image.jpg)
  if (!imagePath.includes('/') && !imagePath.includes('\\')) {
    const baseUrl = import.meta.env.VITE_API_URL || window.location.origin
    return `${baseUrl}/uploads/images/${imagePath}`
  }

  // 그 외의 경우 기본 이미지 반환
  return defaultImage
}

/**
 * 이미지 로드 실패 시 기본 이미지로 대체하는 함수
 * @param {Event} event - 이미지 오류 이벤트
 * @param {string} defaultImage - 기본 이미지 경로
 */
export const handleImageError = (event, defaultImage = '/images/default-trip.jpg') => {
  const imgElement = event.target
  if (imgElement && imgElement.src !== defaultImage) {
    imgElement.src = defaultImage
  }
}
