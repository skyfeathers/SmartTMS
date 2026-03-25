/**
 * 解决Modal loading问题
 * @returns {Element}
 */
export const getContainer = () => {
  return document.getElementsByClassName('ant-spin-container')[0];
};
