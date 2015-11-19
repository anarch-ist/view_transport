<?php
include_once '.\common_files\privilegedUser\PrivilegedUser.php';
try {
    $privUser = PrivilegedUser::getInstance();
    $dataArray = $privUser->getInvoicesForUser()->selectAllData();
    //echo(print_r($dataArray));
    $totalData = count($dataArray);
    $totalFiltered = $totalData;
    $data = array();
    foreach($dataArray as $dataElement) {
        $tmp = array();
        foreach($dataElement as $field) {
            $tmp[] = $field;
        }
        $data[] =$tmp;
    }
    $json_data = array(
        "draw" => intval($_REQUEST['draw']),   // for every request/draw by clientside , they send a number as a parameter, when they recieve a response/data they first check the draw number, so we are sending same number in draw.
        "recordsTotal" => intval($totalData),  // total number of records
        "recordsFiltered" => intval($totalFiltered), // total number of records after searching, if there is no searching then totalFiltered = totalData
        "data" => $data   // total data array
    );
    echo json_encode($json_data);
}
catch (Exception $ex) {
    echo $ex->getMessage();
}
?>