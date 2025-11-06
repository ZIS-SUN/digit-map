<template>
  <div class="chart-panel">
    <div ref="chartRef" class="chart-container"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, defineProps } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  option: {
    type: Object,
    required: true
  },
  height: {
    type: String,
    default: '400px'
  }
})

const chartRef = ref(null)
let chartInstance = null

onMounted(() => {
  initChart()
})

watch(() => props.option, (newOption) => {
  if (chartInstance) {
    chartInstance.setOption(newOption, true)
  }
}, { deep: true })

const initChart = () => {
  if (!chartRef.value) return
  
  chartInstance = echarts.init(chartRef.value)
  chartInstance.setOption(props.option)
  
  // 响应式调整
  window.addEventListener('resize', () => {
    chartInstance?.resize()
  })
}
</script>

<style scoped lang="scss">
.chart-panel {
  width: 100%;
}

.chart-container {
  width: 100%;
  height: v-bind(height);
}
</style>

