import { defineStore } from 'pinia'
import { getSpecimens, getSpecimenDetail } from '@/api/specimen'

export const useSpecimenStore = defineStore('specimen', {
  state: () => ({
    specimens: [],
    currentSpecimen: null,
    queryParams: {
      page: 1,
      pageSize: 12,
      keyword: '',
      category: '',
      hot: false
    },
    total: 0
  }),

  getters: {
    hasMore: (state) => state.specimens.length < state.total
  },

  actions: {
    // 获取标本列表
    async fetchSpecimens(params) {
      try {
        const mergedParams = { ...this.queryParams, ...params }
        const res = await getSpecimens(mergedParams)
        if (res.code === 200) {
          this.specimens = res.data.list
          this.total = res.data.total
          this.queryParams = mergedParams
          return true
        }
      } catch (error) {
        return false
      }
    },

    // 获取标本详情
    async fetchSpecimenDetail(id) {
      try {
        const res = await getSpecimenDetail(id)
        if (res.code === 200) {
          this.currentSpecimen = res.data
          return true
        }
      } catch (error) {
        return false
      }
    },

    // 重置查询条件
    resetQuery() {
      this.queryParams = {
        page: 1,
        pageSize: 12,
        keyword: '',
        category: '',
        hot: false
      }
    }
  }
})

