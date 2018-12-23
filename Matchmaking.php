<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 23/12/2018
 * Time: 14:58
 */

use Ratchet\MessageComponentInterface;
use Ratchet\ConnectionInterface;
use PhpAmqpLib\Connection\AMQPStreamConnection;
use Predis\Client;

class Matchmaking implements MessageComponentInterface
{
    public function __construct($loop)
    {
        $this->loop = $loop;
        $this->clients = new \SplObjectStorage();
        $Logs = [];

        $this->loop->addPeriodicTimer(2, function ()
        {
            $Redis = new Predis\Client([
                'scheme' => 'tcp',
                'host'   => 'node01-int.clusprv.badblock-network.fr',
                'port'   => 62137,
            ],[
                'parameters' => [
                    'password' => "CQVOY51BLBGE5zIOPNh92dGDtvrgNmUf2GUQBMiddOI8egNUTUUMYrghMxoqJ8wBEhi6TqaW5IVaOyUyjEa7WgeyEsPdrcFWjOS25TO1rz16YMv7hv43C9s6s8aJaGvjlecgYR7fhZc5Cx3JTXClWVieTjOTPa30f673N49VUbQmzJVjxoukaDpTM3Dr4wi9tf27a1SU3z7t8TCw8XG8qVR3PkkJHtI1yAYzsGjl5hE407qYUXWbJE4oPfYnyweXX2mgyvsuDXotUFhEs4F2yAgdF4q3rvQfn2lMoE6RrkbbQjG9dc1CC7G72W1khIqvAC7P63xneypVIVULXLdqJJoCwfNEN30evXTrRhFWGUHeFrQt3UKDuY5Q3IlGCbKXZufHCLt9v0d1iUlcEVFYJAFD9R9BTKpI5vfy6zxsqBkylwAQh6ERwakwRkXAVd9QyfevECOU3ZiflJVtFqa2qlo7vcygfxXEC206uFQouZtFzfZ5v0YLbcIxf6ElurP",
                    'database' => 0,
                ]
            ]);

            $Data_cluster = ['DEV', 'PROD'];

            $connection = new AMQPStreamConnection('node01-int.clusprv.badblock-network.fr', 5672, 'shoplinker', 'dpufhsde5x5fukpqbjet4p2wgzrmqwgpmwtrvppajrumaxjwTnmnpqbi3xmgzmsa', 'rabbit');
            $channel = $connection->channel();

            foreach ($Data_cluster as $k => $row){
                $channel->queue_declare('matchmaking.log_' .$row, false, false, false, false);
                $max_number_messages_to_fetch_per_batch = 10000;
                do
                {
                    $message = $channel->basic_get('matchmaking.log_' .$row, true);
                    if($message)
                    {
                        $Body = json_decode($message->body)->message;
                        $Logs = $Redis->get('websock:match_last_logs');
                        $Logs = json_decode($Logs);
                        if ($Logs == null){
                            $Logs = [];
                        }
                        array_push($Logs, $Body);
                        if (count($Logs) > 100){
                            array_shift($Logs);
                        }
                        $Redis->set('websock:match_last_logs', json_encode($Logs));

                        echo "[" . date('Y-m-d H:i:s') . "] ".' [*] Message from RabbitMQ for ' . count($this->clients) .' client queue ' .$row, "\n";
                        foreach($this->clients as $client)
                        {
                            $client->send($Body);
                        }

                        $max_number_messages_to_fetch_per_batch--;
                    }
                }
                while($message && $max_number_messages_to_fetch_per_batch > 0);
            }

            $channel->close();
            $connection->close();
        });

    }

    public function onOpen(ConnectionInterface $connection)
    {
        // Store the new connection to send messages to later
        $this->clients->attach($connection);

        $Redis = new Predis\Client([
            'scheme' => 'tcp',
            'host'   => 'node01-int.clusprv.badblock-network.fr',
            'port'   => 62137,
        ],[
            'parameters' => [
                'password' => "CQVOY51BLBGE5zIOPNh92dGDtvrgNmUf2GUQBMiddOI8egNUTUUMYrghMxoqJ8wBEhi6TqaW5IVaOyUyjEa7WgeyEsPdrcFWjOS25TO1rz16YMv7hv43C9s6s8aJaGvjlecgYR7fhZc5Cx3JTXClWVieTjOTPa30f673N49VUbQmzJVjxoukaDpTM3Dr4wi9tf27a1SU3z7t8TCw8XG8qVR3PkkJHtI1yAYzsGjl5hE407qYUXWbJE4oPfYnyweXX2mgyvsuDXotUFhEs4F2yAgdF4q3rvQfn2lMoE6RrkbbQjG9dc1CC7G72W1khIqvAC7P63xneypVIVULXLdqJJoCwfNEN30evXTrRhFWGUHeFrQt3UKDuY5Q3IlGCbKXZufHCLt9v0d1iUlcEVFYJAFD9R9BTKpI5vfy6zxsqBkylwAQh6ERwakwRkXAVd9QyfevECOU3ZiflJVtFqa2qlo7vcygfxXEC206uFQouZtFzfZ5v0YLbcIxf6ElurP",
                'database' => 0,
            ]
        ]);

        $Logs = $Redis->get('websock:match_last_logs');
        $Logs = json_decode($Logs);
        if ($Logs == null){
            $Logs = [];
        }
        foreach ($Logs as $log){
            $connection->send($log);
        }

        echo "[" . date('Y-m-d H:i:s') . "] "."New connection! ({$connection->remoteAddress})\n";
    }

    public function onMessage(ConnectionInterface $from, $msg)
    {
        $numRecv = count($this->clients) - 1;
        echo sprintf("[" . date('Y-m-d H:i:s') . "] ".'Connection %d sending message "%s" to %d other connection%s'."\n"
            , $from->resourceId, $msg, $numRecv, $numRecv == 1 ? '' : 's');

        foreach($this->clients as $client)
        {
            if($from !== $client)
            {
                $client->send($msg);
            }
        }
    }

    public function onClose(ConnectionInterface $conn)
    {
        $this->clients->detach($conn);
        echo "[" . date('Y-m-d H:i:s') . "] "."Connection {$conn->resourceId} has disconnected\n";
    }

    public function onError(ConnectionInterface $conn, \Exception $e)
    {
        echo "[" . date('Y-m-d H:i:s') . "] "."An error has occurred: {$e->getMessage()}\n";
    }

}