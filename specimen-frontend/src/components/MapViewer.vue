<template>
  <div class="map-viewer">
    <div id="map-container" ref="mapContainer"></div>
    <!-- 标注信息弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      title="标本信息" 
      width="400px"
      :close-on-click-modal="true"
    >
      <div v-if="currentMarker" class="marker-info">
        <el-image 
          :src="currentMarker.imageUrl" 
          fit="cover"
          style="width: 100%; height: 200px; margin-bottom: 15px;"
        />
        <p><strong>名称：</strong>{{ currentMarker.name }}</p>
        <p><strong>科属：</strong>{{ currentMarker.family }}</p>
        <p><strong>采集人：</strong>{{ currentMarker.collector }}</p>
        <p><strong>位置：</strong>{{ currentMarker.location }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps, watch } from 'vue'
import AMapLoader from '@amap/amap-jsapi-loader'

const props = defineProps({
  markers: {
    type: Array,
    default: () => []
  },
  center: {
    type: Array,
    default: () => [116.397428, 39.90923]
  },
  zoom: {
    type: Number,
    default: 13
  },
  editable: {
    type: Boolean,
    default: false
  }
})

const mapContainer = ref(null)
let map = null
const markerInstances = []
const dialogVisible = ref(false)
const currentMarker = ref(null)

onMounted(() => {
  initMap()
})

watch(() => props.markers, (newMarkers) => {
  if (map) {
    clearMarkers()
    addMarkers(newMarkers)
  }
}, { deep: true })

const initMap = () => {
  AMapLoader.load({
    key: 'YOUR_AMAP_KEY', // 需要替换为实际的高德地图key
    version: '2.0',
    plugins: ['AMap.Scale', 'AMap.ToolBar']
  }).then((AMap) => {
    map = new AMap.Map('map-container', {
      zoom: props.zoom,
      center: props.center,
      viewMode: '3D'
    })

    // 添加控件
    map.addControl(new AMap.Scale())
    map.addControl(new AMap.ToolBar())

    // 添加标注
    addMarkers(props.markers)
  }).catch(e => {
    console.error('地图加载失败', e)
  })
}

const addMarkers = (markers) => {
  if (!map || !window.AMap) return

  markers.forEach(marker => {
    const markerInstance = new window.AMap.Marker({
      position: [marker.longitude, marker.latitude],
      title: marker.name,
      draggable: props.editable
    })

    markerInstance.on('click', () => {
      currentMarker.value = marker
      dialogVisible.value = true
    })

    if (props.editable) {
      markerInstance.on('dragend', (e) => {
        const position = e.target.getPosition()
        emit('markerDrag', {
          id: marker.id,
          longitude: position.lng,
          latitude: position.lat
        })
      })
    }

    map.add(markerInstance)
    markerInstances.push(markerInstance)
  })
}

const clearMarkers = () => {
  if (map && markerInstances.length > 0) {
    map.remove(markerInstances)
    markerInstances.length = 0
  }
}
</script>

<style scoped lang="scss">
.map-viewer {
  width: 100%;
  height: 100%;
  position: relative;
}

#map-container {
  width: 100%;
  height: 100%;
}

.marker-info {
  p {
    margin: 10px 0;
    font-size: 14px;
    color: #606266;
  }
}
</style>

