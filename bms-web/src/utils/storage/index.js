// 封装存储方法
/**
 * @param {string} key 存储的key
 * @param {any} value 需要存储的值
 * @param {boolean} isLocal 是否本地存储
 */
export function setCache(key, value, isLocal = true) {

  const val = JSON.stringify(value)
  if (isLocal) {
    localStorage.setItem(key, val)
  } else {
    sessionStorage.setItem(key, val)
  }
}

/**
 * @param {string} key 存储的key
 * @param {boolean} isLocal 是否本地存储
 * @returns 存储的值
 */
export function getCache(key, isLocal = true) {
  let res = ''
  if (isLocal) {
    res = JSON.parse(localStorage.getItem(key))
  } else {
    res = JSON.parse(sessionStorage.getItem(key))
  }

  return res
}

/**
 * @param {string} key 存储的key
 * @param {boolean} isLocal 是否本地存储
 */
export function removeCache(key, isLocal = true) {
  if (isLocal) {
    localStorage.removeItem(key)
  } else {
    sessionStorage.removeItem(key)
  }
}
