<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class PasswordSecurityController extends Controller
{
    public function show2faForm(Request $request){

        $user = Auth::user();

        $google2fa_url = "";
        if($user->passwordSecurity()->exists()){
            $google2fa = app('pragmarx.google2fa');
            $google2fa_url = $google2fa->getQRCodeGoogleUrl(
                '5Balloons 2A DEMO',
                $user->email,
                $user->passwordSecurity->google2fa_secret
            );
        }
        $data = array(
            'user' => $user,
            'google2fa_url' => $google2fa_url
        );

        return view('auth.2fa')->with('data', $data);
    }

    public function generate2faSecret(Request $request){

        $user = Auth::user();
        // Initialise the 2FA class
        $google2fa = app('pragmarx.google2fa');

        // Add the secret key to the registration data
        PasswordSecurity::create([
            'user_id' => $user->id,
            'google2fa_enable' => 0,
            'google2fa_secret' => $google2fa->generateSecretKey(),
        ]);

        return redirect('/2fa')->with('success',"Secret Key is generated, Please verify Code to Enable 2FA");
    }
}
