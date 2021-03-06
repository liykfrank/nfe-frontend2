DELETE FROM bsplink_config;

INSERT INTO bsplink_config (id, config)
VALUES (
    'FILE',
    '{"fileNamePatterns":["^[A-Z][A-Z0-9][a-z0-9]{2}[A-Z0-9]{3}[0-6]_[0-9]{8}.*$","^[A-Z][A-Z0-9][a-z0-9]{2}[0-9]{7}[0-6]_[0-9]{8}.*","^[A-Z][A-Z0-9][a-z0-9]{2}BSP_[0-9]{8}.*","^[A-Z][A-Z0-9][a-z0-9]{2}CRS[A-Z]{4}_[0-9]{8}.*","^[A-Z][A-Z0-9][a-z0-9]{2}DPC_[0-9]{8}.*","^[A-Z][A-Z0-9][a-z0-9]{2}EARS_[0-9]{8}.*","^[A-Z][A-Z0-9][a-z0-9]{2}GRP[A-Z0-9]+_[0-9]{8}.*","^[A-Z][A-Z0-9][a-z0-9]{2}IATA_[0-9]{8}.*","^[A-Z][A-Z0-9][a-z0-9]{2}TTT[A-Z0-9]+_[0-9]{8}.*"],"allowedFileExtensions":["AED","AGTD","AM2","AMX","asv","atc","ATL","aug","AX","BHD","bmp","BRU","BSM","BSP","C","CAR","CC","CED","CFV","CIRC","CLAS","CLUB","CMAS","csv","CTL","dat","dbf","DEL","doc","docx","dom","dot","EARS","EEC","eml","FIF","FLGX","GDSL","GIF","GL1","GLE","gpg","GVT","HNM","HRG","HTM","IATA","ICT","int","jpeg","jpg","KYM","LUX","m01","m02","m03","m04","m05","m06","m07","m08","m09","m10","m11","m12","MOR","mp3","msg","MVV","new","NUR","odt","oft","old","OR1","orig","out","p","pd","pdf","pgp","png","pptx","ps","PTG","R","rar","RESI","RG","RL","rtf","SABR","ST1","STU","T","tar","TCM","tif","TRA","TRV","txt","unl","VIIC","VTG","W","WAG","wav","WEBL","wire","WSPN","xcf","xls","xlsm","xlsx","xlt","xml","xps","Z","zip",""],"maxDownloadTotalFileSizeForMultipleFiles":-1,"maxUploadFilesNumber":100,"maxDownloadFilesNumber":-1}'
)
