import { reactive } from 'vue'

const option = reactive({
  color: ['#f0a040'],
  title: {
    text: '季度会员统计图'
  },
  xAxis: {
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar',
    }
  ]
})

export default option