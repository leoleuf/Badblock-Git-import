<?php

/*
 The MIT License (MIT)

Copyright (c) 2014 Christian Klisch

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

namespace App\Middlewares;

use Psr\Http\Message\ServerRequestInterface as Request;
use Psr\Http\Message\ResponseInterface as Response;
use Slim\Http\Body;

/**
 * Minify-Middleware is a summary of stackoverflow answers to reduce html traffic
 * by removing whitespaces, tabs, empty lines and comments.
 * */
class MinifyMiddleware
{
    /**
     * minify html content
     *
     * @param string $html
     * @return string
     */
    private function minifyHTML($html)
    {
        /*$search = array('/(?:(?:\/\*(?:[^*]|(?:\*+[^*\/]))*\*+\/)|(?:(?<!\:|\\\|\'|\")\/\/.*))/', '/\n/', '/\>[^\S ]+/s', '/[^\S ]+\</s', '/(\s)+/s', '/<!--.*?-->/');
        $replace = array(' ', ' ', '>', '<', '\\1', '');

        $squeezedHTML = preg_replace($search, $replace, $html);*/

        return $html;

    }

    /**
     * @param Request $request
     * @param Response $response
     * @param callable $next
     * @return static
     */
    public function __invoke(Request $request, Response $response,callable $next)
    {
        return $next($request, $response);
       // $oldBody = $response->getBody();

      /*  $EX = explode("/", $request->getUri()->getPath());
        if ($EX[0] == 'shop' || $EX[1] == 'shop'){
            return $next($request, $response);
        }elseif ($oldBody != null && $oldBody->getSize() > 0) {
            $minifiedBodyContent = $this->minifyHTML($response->getBody()->__toString());

            $newBody = new Body(fopen('php://temp', 'r+'));

            //write the minified html content to the new \Slim\Http\Body instance
            $newBody->write($minifiedBodyContent);

            return $response->withBody($oldBody);
        }else{
            return $next($request, $response);
        }*/
    }
}

