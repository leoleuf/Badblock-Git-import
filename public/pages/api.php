<?php

use App\Database;

if(!$_GET)
    return http_response_code(404);

$params = array_map('strip_tags', $_GET);

if(isset($params['player'])){

    $sql = "SELECT votes_nb FROM votes WHERE user_uuid = ?";

    if(isset($params['month']) && isset($params['year']))
        $sql .= " AND month = ? AND year = ";
    elseif(isset($params['year']))
        $sql .= " AND year = ?";
    elseif (isset($params['month']))
        $sql .= " AND month = ?";

    $values = [];
    foreach($params as $key => $value) {
        unset($params[$key]);
        array_push($values, $value);
    }

    $db = new Database();
    $result = $db->prepare($sql);
    $result->execute($values);
    $result = $result->fetch();

    if($result)
        echo json_encode($result);
    else
        echo json_encode("[]");


}

else
    return http_response_code(404);