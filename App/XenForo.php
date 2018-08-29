<?php

namespace App;

class XenForo
{

    public function __construct($container, $config)
    {
        $this->container = $container;
        $this->guzzle = $container->guzzle;
        /*
         * config :
         * endpoint
         * hash
         */
        $this->config = $config;
    }

    public function hash()
    {
        date_default_timezone_set('Europe/London');
        $time = date('Y-m-d h:i');
        $time = hash("gost", $time);
        $key = md5($time);

        return $key;
    }

    public function doGetRequest($action)
    {
        return $this->guzzle->get($this->config['endpoint'] . '?' . $action . '&hash=' . $this->hash());
    }


    public function getParsedBody($body)
    {
        return json_decode($body, 1);
    }

    /**
     * Get list of all posts associed to news
     *
     * Api request: {endpoint}?action=getThreads&node_id=113&order_by=post_date&hash=SKiaGWKSRojFBJaLjZbtSox4QWpRFfkS
     */
    public function getAllNewsPosts()
    {
        $rep = $this->doGetRequest('action=getThreads&node_id=113&order_by=post_date&limit=999');

        return $this->getParsedBody($rep->getBody());
    }

    /**
     * @param $postId
     * @return mixed
     */
    public function getNewPost($postId)
    {
        $rep = $this->doGetRequest('action=getPost&value=' . $postId);

        return $this->getParsedBody($rep->getBody());
    }

    public function getUser($username)
    {
        $rep = $this->doGetRequest('action=getUser&value=' . $username);

        return $this->getParsedBody($rep->getBody());
    }

    public function getPostsInThread($threadId)
    {
        $rep = $this->doGetRequest("action=getPosts&thread_id={$threadId}");

        $posts = $this->getParsedBody($rep->getBody());
        unset($posts['posts'][0]);

        return $posts;
    }

    public function createPost($message, $userId, $threadId)
    {
        $rep = $this->doGetRequest("action=createPost&message={$message}&thread_id={$threadId}&grab_as={$userId}");

        return $this->getParsedBody($rep->getBody());
    }

    public function addGroup($username, $group)
    {

        try {
            $rep = $this->doGetRequest('action=editUser&user=' . $username . '&add_groups=' . $group);

            return $this->getParsedBody($rep->getBody());
        } catch (\Exception $exception) {
            return false;


        }

    }

    /**
     * @param $username
     * @param $password
     * @param $ip
     * @return mixed
     */
    public function getLogin($username, $password, $ip,$tfa = false)
    {
        try {
            if ($tfa){
                $data = $this->guzzle->get($this->config['endpoint'] . '?' . 'action=login&username=' . $username . '&password=' . $password . '&ip_address=' . '127.0.0.1' . '&hash=' . $this->hash(). "&tfa=true");
            }else{
                $data = $this->guzzle->get($this->config['endpoint'] . '?' . 'action=login&username=' . $username . '&password=' . $password . '&ip_address=' . '127.0.0.1' . '&hash=' . $this->hash());
            }
        } catch (\GuzzleHttp\Exception\ClientException $e) {
            $response = $e->getResponse();
            $responseBody = $e->getResponse()->getBody(true)->getContents();
            $responseBody = json_decode($responseBody);

            if ($response && $response->getStatusCode() == 400) {
                if($responseBody->error == 5){
                    return "bad";
                }elseif($responseBody->error == 25){
                    return "tfa";
                }
            }
        }
        return $this->getParsedBody($data->getBody());
    }
}