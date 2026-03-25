
export const isImageByType = (fileType) => {
    return ['png', 'jpg','jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(fileType.toLowerCase()) !== -1
};

export const isImageByName = (fileName) => {
    let index = fileName.lastIndexOf('.')
    if(index < 1){
        return false;
    }
    let fileType = fileName.substr(index + 1)
    return isImageByType(fileType)
};
