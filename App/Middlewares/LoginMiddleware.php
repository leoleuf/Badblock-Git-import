<?php
namespace App\Middlewares;
use Dflydev\FigCookies\FigRequestCookies;

class LoginMiddleware
{

	public function __invoke($request, $response, $next)
	{

		$cookie = FigRequestCookies::get($request, 'badblockauth_session');

		return $next($request, $response);
	}
}