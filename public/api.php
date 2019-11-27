<?php

include_once '../App/Helper.php';

if($_POST){

    $params = array_map('strip_tags', $_POST);

    /**
     * VOTE API
     */

    /**
     * Step 1 : Check if user with provided name exists in DB
     * Request type : POST
     * Request params : exists (string, player name)
     */
    if(isset($params['exists'])){

        $result = db()->prepare('SELECT name FROM users WHERE name = ?');
        $result->execute([$params['exists']]);
        $result = $result->fetchAll();

        if(count($result) >= 1)
            echo 1;
        else
            echo 0;
        return;
    }

    /**
     * Step 2 : Call the right vote website API and interrogate it
     * Request type : POST
     * Request params : platform (String), username (String)
     * Check if the provided username has voted onto the requested vote platform platform param
     */
    if(isset($params['username']) && isset($params['platform'])){

        $config = getConfig()['vote_platforms'];

        /**
         * TODO
        *   Use cURL instead of file_get_contents
         * Improve response returning to add informations
         * Discover new vote platforms (TopG not usable)
         **/

        /**
         * What is this code doing ?
         * It checks one by one if the player has voted onto at least one platform
         * Why ?
         * Because no need to use a precise platform to deliver award
         */
        //Checking top-serveurs.net
        $response = json_decode(file_get_contents("https://api.top-serveurs.net/v1/votes/check?server_token=".$config['top-serveurs']['api_token']."&playername=".$params['username']));
        if($response['success'])
            return true;

        //Checking serveurminecraft.org
        $response = file_get_contents("https://www.serveursminecraft.org/sm_api/peutVoter.php?id=".$config['serveursminecraft']['api_token']."&ip=".$_SERVER['REMOTE_ADDR']);
        if($response)
            return true;

        return false;
    }

    return http_response_code(404);
}

else if ($_GET) {

    $params = array_map('strip_tags', $_GET);

    /**
     * JAVA WEB API
     */
    if (isset($params['player'])) {

        $sql = "SELECT votes_nb FROM votes WHERE user_uuid = ?";

        if (isset($params['month']) && isset($params['year']))
            $sql .= " AND month = ? AND year = ";
        elseif (isset($params['year']))
            $sql .= " AND year = ?";
        elseif (isset($params['month']))
            $sql .= " AND month = ?";

        $values = [];
        foreach ($params as $key => $value) {
            unset($params[$key]);
            array_push($values, $value);
        }

        $result = db()->prepare($sql);
        $result->execute($values);
        $result = $result->fetch();

        if ($result)
            echo json_encode($result);
        else
            echo json_encode("[]");


    }

    return http_response_code(404);
}

return http_response_code(404);