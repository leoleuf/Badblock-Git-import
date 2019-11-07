<?php

    if($_GET) {

        $params = array_map('strip_tags', $_GET);

        /**
         *
         * HERE IS THE API
         *
         */
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

            $credentials = [
                "host" => "127.0.0.1",
                "port" => "3306",
                "dbname" => "badblock_stats",
                "user" => "root",
                "password" => ""
            ];

            $pdo = new PDO("mysql:host=" . $credentials['host'] . ";port=".$credentials['port'].";dbname=" . $credentials['db'], $credentials['user'], $credentials['password'], [
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_OBJ
            ]);

            $result = $pdo->prepare($sql);
            $result->execute($values);
            $result = $result->fetch();

            if($result)
                echo json_encode($result);
            else
                echo json_encode("[]");


        }

        else
            return http_response_code(404);
    }

    /**
     *
     * HERE IS THE VOTE CONFIRMATION
     *
     */

    ?>

<html>
    <head>



    </head>

    <body>



    </body>
</html>
