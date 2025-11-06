import { defineStore } from 'pinia'
import { getMapMarkers } from '@/api/map'

export const useMapStore = defineStore('map', {
  state: () => ({
    markers: [],
    currentMarker: null,
    mapCenter: [116.397428, 39.90923], // 默认中心点（北京）
    mapZoom: 13
  }),

  getters: {
    markerCount: (state) => state.markers.length
  },

  actions: {
    // 获取地图标注列表
    async fetchMarkers(params) {
      try {
        const res = await getMapMarkers(params)
        if (res.code === 200) {
          this.markers = res.data
          return true
        }
      } catch (error) {
        return false
      }
    },

    // 设置当前标注
    setCurrentMarker(marker) {
      this.currentMarker = marker
    },

    // 设置地图中心
    setMapCenter(center) {
      this.mapCenter = center
    },

    // 设置地图缩放级别
    setMapZoom(zoom) {
      this.mapZoom = zoom
    },

    // 清空标注缓存
    clearMarkers() {
      this.markers = []
      this.currentMarker = null
    }
  }
})

