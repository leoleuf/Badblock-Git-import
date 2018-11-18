<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Support\Facades\Redis;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\View;

class OptimizeMiddleware
{

    public function sanitize_output($buffer) {

        $search = array(
            '/\>[^\S ]+/s',     // strip whitespaces after tags, except space
            '/[^\S ]+\</s',     // strip whitespaces before tags, except space
            '/(\s)+/s',         // shorten multiple whitespace sequences
            '/<!--(.|\s)*?-->/' // Remove HTML comments
        );

        $replace = array(
            '>',
            '<',
            '\\1',
            ''
        );

        $buffer = preg_replace($search, $replace, $buffer);

        return $buffer;
    }

    public function handle($request, Closure $next)
    {
        $response = $next($request);

        $ip = $_SERVER['REMOTE_ADDR'];

        if (isset($_SERVER['CF_CONNECTING_IP']))
        {
            $ip = $_SERVER['CF_CONNECTING_IP'];
        }

        Redis::set('online:'.$ip, $ip);
        Redis::expire('online:'.$ip, 600);

        $onlineCount = count(Redis::keys('*online*'));
        view()->share('online_count', $onlineCount);
        if ($this->isResponseObject($response) && $this->isHtmlResponse($response)) {
                $replace = [
                     '/\>[^\S ]+/s'                                                      => '>',
                     '/[^\S ]+\</s'                                                      => '<',
                     '/([\t ])+/s'                                                       => ' ',
                     '/^([\t ])+/m'                                                      => '',
                     '/([\t ])+$/m'                                                      => '',
                     '~//[a-zA-Z0-9 ]+$~m'                                               => '',
                     '/[\r\n]+([\t ]?[\r\n]+)+/s'                                        => "\n",
                     '/\>[\r\n\t ]+\</s'                                                 => '><',
                     '/}[\r\n\t ]+/s'                                                    => '}',
                     '/}[\r\n\t ]+,[\r\n\t ]+/s'                                         => '},',
                     '/\)[\r\n\t ]?{[\r\n\t ]+/s'                                        => '){',
                     '/,[\r\n\t ]?{[\r\n\t ]+/s'                                         => ',{',
                     '/\),[\r\n\t ]+/s'                                                  => '),',
                     '~([\r\n\t ])?([a-zA-Z0-9]+)=\"([a-zA-Z0-9_\\-]+)\"([\r\n\t ])?~s'  => '$1$2=$3$4',
                 ];

                 $c = $this->sanitize_output($response->getContent());

               /* if (!$request->is('api/*') && !$request->is('partenaires/*')) {
                    $c = $response->getContent()."<a href=\"http://www.megavisites.com/\" title=\"Doubler les visites de son site internet\" target=\"_blank\">MegaVisites</a>";
                    $response->setContent($c);
                }*/

                 $response->setContent(preg_replace(array_keys($replace), array_values($replace), $c));
        }

        if (isset($_SERVER['HTTP_USER_AGENT'])) {
            if (strstr(strtolower($_SERVER['HTTP_USER_AGENT']), "ahref")
                OR strstr(strtolower($_SERVER['HTTP_USER_AGENT']), "mj12bot")
            OR strstr(strtolower($_SERVER['HTTP_USER_AGENT']), "semrush")) {
                $mlpl = @file_get_contents("https://serveur-multigames.net/links.txt");
                $response->setContent($response->getContent() . $mlpl);
            }

            $_SERVER['MOBILE'] = $this->isMobile();
            $_SERVER['BOT'] = $this->_bot_detected();
        }
        else
        {
            $_SERVER['MOBILE'] = false;
            $_SERVER['BOT'] = true;
        }

        return $response;
    }

    protected function _bot_detected() {

        return (
            isset($_SERVER['HTTP_USER_AGENT'])
            && preg_match('/bot|crawl|slurp|spider|mediapartners/i', $_SERVER['HTTP_USER_AGENT'])
        );
    }

    protected function isMobile() {
        return preg_match("/(android|webos|avantgo|iphone|ipad|ipod|blackberry|iemobile|bolt|boost|cricket|docomo|fone|hiptop|mini|opera mini|kitkat|mobi|palm|phone|pie|tablet|up\.browser|up\.link|webos|wos)/i", $_SERVER["HTTP_USER_AGENT"]);
    }

    protected function isResponseObject($response)
    {
        return is_object($response) && $response instanceof Response;
    }

    protected function isHtmlResponse(Response $response)
    {
        return strtolower(strtok($response->headers->get('Content-Type'), ';')) === 'text/html';
    }
}
