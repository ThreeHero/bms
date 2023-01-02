/**
 * * 数组末尾追加元素 *
 * @author xiaoliu
 * @param {any[]} arr 
 * @param  {...any} args 传入需要追加的元素
 * @returns 0{number} 返回新数组的长度
 */
function push(arr, ...args) {
  for (const arg of args) {
    arr[arr.length] = arg
  }
  return arr.length
}

/**
 * * 删除数组最后一位元素 *
 * @author xiaoliu
 * @param {any[]} arr 
 * @returns {any | undefined} 返回删除的元素
 */
function pop(arr) {
  if (arr.length <= 0) return undefined
  const deleteItem = arr[arr.length - 1] 
  arr.length--
  return deleteItem
}

/**
 * * 数组开头添加元素 *
 * @author xiaoliu
 * @param {any[]} arr 
 * @param  {...any} args 传入需要添加的元素
 * @returns {number} 返回新数组的长度
 */
function unshift(arr, ...args) {
  const tempArr = [...arr]
  let index = 0
  for (const arg of args) {
    arr[index++] = arg
  }
  for (const temp of tempArr) {
    arr[index++] = temp
  }
  return index
}

/**
 * * 删除数组首位元素 *
 * @author xiaoliu
 * @param {any[]} arr 
 * @returns {any | undefined} 返回删除的元素
 */
function shift(arr) {
  if (arr.length <= 0) return undefined
  const tempArr = [...arr]
  for (let i = 1; i < tempArr.length; i++) {
    arr[i - 1] = tempArr[i]
  }
  arr.length--
  return tempArr[0]
}

/**
 * * 反转数组 *
 * @author xiaoliu
 * @param {any[]} arr 
 * @returns {any[]} 反转后的数组
 */
function reverse(arr) {
  const tempArr = [...arr]
  const len = tempArr.length
  for (let i = len - 1; i >= 0; i--) {
    arr[len - 1 - i] = tempArr[i]
  }
  return arr
}

// function sort(arr, callback) {

// }

// ! 仿数组length属性的length方法
function length(arr) {
  let len = 0
  for (const i in arr) {
    len++
  }
  return len
}


const arr = [1, 2, 3, 11, 'a', '0']

