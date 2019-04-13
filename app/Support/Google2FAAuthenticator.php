<?php
/**
 * Created by PhpStorm.
 * User: Fragan
 * Date: 23/03/2019
 * Time: 11:59
 */

namespace App\Support;

use PragmaRX\Google2FALaravel\Support\Authenticator;
use Illuminate\Http\Request;

class Google2FAAuthenticator extends Authenticator
{
    protected function canPassWithoutCheckingOTP()
    {
        if (empty($this->getUser()->passwordSecurity))
            return true;
        if($this->checkCookieTFA() == "1")
            return true;
        return
            !$this->getUser()->passwordSecurity->google2fa_enable ||
            !$this->isEnabled() ||
            $this->noUserIsAuthenticated() ||
            $this->twoFactorAuthStillValid();
    }

    protected function getGoogle2FASecretKey()
    {
        $secret = $this->getUser()->passwordSecurity->{$this->config('otp_secret_column')};
        if (is_null($secret) || empty($secret)) {
            throw new InvalidSecretKey('Secret key cannot be empty.');
        }
        return $secret;
    }

    protected function checkCookieTFA()
    {
        return \Illuminate\Support\Facades\Cookie::get('TFA');
    }
}