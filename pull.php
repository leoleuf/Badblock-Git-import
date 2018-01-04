<?php
chdir('/home/web/dev-web/badblock-website');
shell_exec('git pull http://web_bot:kPnZSY3DW9gCCnyBxrA2Fm6eAa3tcafe@vps446463.ovh.net/Website/badblock-website.git');
shell_exec('php composer.phar install');
shell_exec('php composer.phar update');

$data = array("username" => "Logger Site","embeds" => array(0 => array(
            "url" => "https://dev-web.badblock.fr",
            "title" => "GitLab Puller ",
            "description" => "Site Web de démo update !",
            "color" => 65280
        )));
		
		        $curl = curl_init("https://discordapp.com/api/webhooks/373808432324542464/g_ZJQXYA0yPj7LyHebSQZA14eAbLxB7w8idL50weFHX-rSGpdI-cu-fiu0gbHl9BIa8F");
        curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_exec($curl);