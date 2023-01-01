// 需要id和pid
function useToTree(data) {
  const tree = []

  // 循环遍历数据 如果没有pid 就代表着是根
  data.forEach(route => {
    if (route.pid === null) {
      tree.push(route)
      tree.at(-1).children = []
    } else {
      tree.forEach(item => {
        if (item.id === route.pid) {
          item.children.push(route)
        }
      })
    }
  })
  return tree
}

export default useToTree