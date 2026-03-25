import FileSaver from 'file-saver';
import * as XLSX from 'xlsx';

/**
 * 将JS数据的数组转换为工作表导出，
 * @param dataList 数据列表
 * @param columns table columns
 * @param fileName 文件名
 * @param title 导出文件标题
 */
export const customExport = (dataList, columns, fileName, title) => {
  let exportList = [];
  let exportTitle = [];
  columns.forEach(column => {
    exportTitle.push(column.title);
  });
  exportList.push(exportTitle);

  dataList.forEach(data => {
    let exportData = [];
    columns.forEach(column => {
      exportData.push(data[column.dataIndex]);
    });
    exportList.push(exportData);
  });

  var ws = XLSX.utils.aoa_to_sheet(exportList, {});
  // if (title) {
  //   let colSpan = exportList[0].length - 1;
  //   ws['!merges'] = [{ s: { c: 0, r: 0 }, e: { c: colSpan, r: 0 } }];
  // }
  let wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws);
  var wbout = XLSX.write(wb, {
    bookType: 'xlsx',
    bookSST: true,
    type: 'array'
  });
  try {
    FileSaver.saveAs(
      new Blob([wbout], { type: 'application/octet-stream;charset=utf-8' }),
      fileName
    );
  } catch (e) {
    console.log(e);
  }
};

