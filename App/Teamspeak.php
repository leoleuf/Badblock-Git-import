<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 26/10/2017
 * Time: 19:28
 */

namespace App;

use \ts3admin;
use Noodlehaus\Exception;

class Teamspeak
{

    public function __construct($container, $config)
    {
        $this->container = $container;
        $this->redis = $container->redis;
        $this->ip = $config->ip;
        $this->port = $config->port;
        $this->username = $config->username;
        $this->password = $config->password;
        $this->query_port = $config->query_port;
        $this->client = new ts3admin($this->ip, $this->query_port);
    }

    public function connection(){
        if($this->client->getElement('success', $this->client->connect())) {
            //Login sur le query
            $login = $this->client->login($this->username, $this->password);
            if ($login['success'] == true){
                $this->client->selectServer($this->port);
            }else{
                $this->container->log->error('"App/TeamSpeak"',' Bad login ! User : ' . $this->username);
                throw new Exception('TeamSpeak : Bad Login');
            }
        }else{
            $login = $this->client->login($this->username, $this->password);
            if ($login['success'] == true){
                $this->client->selectServer($this->port);
            }
        }
    }

    public function online(){
        $this->connection();
        return $this->client->serverInfo()["data"]['virtualserver_clientsonline'];
    }

    //Create channel Teamspeak
    public function createChannel($Uid, $Name, $Password){
        $this->connection();
        $data = array();
        $data['CHANNEL_NAME'] = $Name;
        $data['CHANNEL_PASSWORD'] = $Password;
        $data['CHANNEL_FLAG_PERMANENT'] = 1;
        $data['CPID'] = 230;
        $data['CHANNEL_DESCRIPTION'] = "";

        $ChannelId = $this->client->channelCreate($data);

        $ClientId = $this->client->clientGetDbIdFromUid($Uid);
        //Proprio du canal
        $this->client->channelGroupAddClient(5, $ChannelId['data']['cid'], $ClientId['data']['cldbid']);

        return $ChannelId;
    }

    public function deleteChannel($Uid){
        $this->connection();
        $Del = $this->client->channelDelete($Uid, 1);
        return $Del;
    }

    public function customGroup($Name){
        $this->connection();
        $Id = $this->client->serverGroupAdd($Name, 1);

        $permissions = array();
        $permissions['b_channel_create_temporary'] = array(1, 0, 0);
        $permissions['b_channel_create_with_password'] = array(1, 0, 0);
        $permissions['b_channel_create_modify_with_codec_speex8'] = array(1, 0, 0);
        $permissions['b_channel_create_modify_with_codec_speex16'] = array(1, 0, 0);
        $permissions['b_channel_create_modify_with_codec_speex32'] = array(1, 0, 0);
        $permissions['b_channel_create_modify_with_codec_opusvoice'] = array(1, 0, 0);
        $permissions['b_channel_create_modify_with_codec_opusmusic'] = array(1, 0, 0);
        $permissions['i_channel_create_modify_with_codec_maxquality'] = array(10, 0, 0);
        $permissions['i_channel_create_modify_with_codec_latency_factor_min'] = array(1, 0, 0);
        $permissions['i_channel_modify_power'] = array(10, 0, 0);
        $permissions['b_channel_join_permanent'] = array(1, 0, 0);
        $permissions['b_channel_join_semi_permanent'] = array(1, 0, 0);
        $permissions['b_channel_join_temporary'] = array(1, 0, 0);
        $permissions['i_channel_join_power'] = array(6, 0, 0);
        $permissions['i_channel_subscribe_power'] = array(10, 0, 0);
        $permissions['i_channel_description_view_power'] = array(10, 0, 0);
        $permissions['i_channel_max_depth'] = array(5, 0, 0);
        $permissions['i_group_needed_modify_power'] = array(74, 0, 0);
        $permissions['i_group_needed_member_add_power'] = array(20, 0, 0);
        $permissions['i_group_needed_member_remove_power'] = array(60, 0, 0);
        $permissions['b_group_is_permanent'] = array(1, 0, 0);
        $permissions['i_group_sort_id'] = array(50001, 0, 0);
        $permissions['i_group_show_name_in_tree'] = array(1, 0, 0);
        $permissions['i_client_needed_kick_from_server_power'] = array(15, 0, 0);
        $permissions['i_client_needed_kick_from_channel_power'] = array(10, 0, 0);
        $permissions['i_client_needed_ban_power'] = array(30, 0, 0);
        $permissions['i_client_needed_move_power'] = array(15, 0, 0);
        $permissions['i_client_complain_power'] = array(10, 0, 0);
        $permissions['i_client_needed_complain_power'] = array(20, 0, 0);
        $permissions['i_client_private_textmessage_power'] = array(10, 0, 0);
        $permissions['b_client_channel_textmessage_send'] = array(1, 0, 0);
        $permissions['i_client_talk_power'] = array(28, 0, 0);
        $permissions['i_client_poke_power'] = array(10, 0, 0);
        $permissions['i_client_whisper_power'] = array(1, 0, 0);
        $permissions['i_client_needed_whisper_power'] = array(70, 0, 0);
        $permissions['i_client_max_clones_uid'] = array(0, 0, 0);
        $permissions['i_client_max_avatar_filesize'] = array(-1, 0, 0);
        $permissions['i_client_max_channel_subscriptions'] = array(-1, 0, 0);
        $permissions['b_client_request_talker'] = array(1, 0, 0);

        $Succes = $this->client->serverGroupAddPerm($Id['data']['sgid'], $permissions);

        return $Id['data']['sgid'];
    }

    public function removeGroup($Name){
        $this->connection();
        $Id = $this->client->serverGroupDelete($Name, 1);
        return $Id;
    }

    public function addtogroup($Uid, $GroupId){
        $this->connection();
        $ClientId = $this->client->clientGetDbIdFromUid($Uid);
        $Id = $this->client->serverGroupAddClient($GroupId, $ClientId['data']['cldbid']);

        return true;
    }


    public function removeClient($Uid){
        $this->connection();
        $ClientId = $this->client->clientGetDbIdFromUid($Uid);

        $this->client->clientDbDelete($ClientId['data']['cldbid']);

        return true;
    }


}