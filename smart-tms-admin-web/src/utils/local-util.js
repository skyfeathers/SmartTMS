import localStorageKeyConst from "/@/constants/local-storage-key-const";

/**
 * localStorage存取
 * @param key
 * @param value
 */
export const localSave = (key, value) => {
  localStorage.setItem(key, value);
};

export const localRead = (key) => {
  return localStorage.getItem(key) || '';
};

export const localClear = () => {
  // localStorage.clear();
  let values = Object.values(localStorageKeyConst).filter(e=>e != localStorageKeyConst.UNIQUE_CODE);
  values.forEach(e=>localStorage.removeItem(e))
};



