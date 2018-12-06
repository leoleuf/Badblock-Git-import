<?php
/**
 * Created by PhpStorm.
 * User: mathieu
 * Date: 17/03/2018
 * Time: 18:49
 */

namespace App\Http\Controllers\moderation;


use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class SanctionController extends Controller
{
    public function index()
    {

        $raison = [
            'Sanction Particuliere',
            'Spam / Flood / Couleur',
            'Insulte / Provocation / Citation de serveur',
            'Question (ou insulte) en /modo Inutile',
            ' Farm AFK - Anti AFK (skyblock / faction)',
            'Diffamation',
            'Discrimination (homophobie, racisme, antisémitisme ...)',
            'Insulte Staff / Menace staff / Irrespect staff / Mensonge staff',
            'Menace (DDOS / Hack)',
            'Insulte Serveur / Communautée',
            'Publicité / Vente',
            'Recrutement staff',
            'Anti-Jeux / Spanwkill',
            'Cheat',
            'Aveu de cheat',
            'Refus de Vérif',
            'Déconnection en vérif',
            'Cheat  + (Irrespect Staff / Perte de temps / Troll)',
            'Skin Injurieux ou Obscene',
            'Pillage skyblock/Duplication',
            'Plot Obscene , religieux ou non réglementé',
            'Pseudo Innaprioprié / Choquant / Difamatoir / Insultant / Obscene'
        ];

        return view('section.mod.tx-sanction', ['Raison' => $raison]);
    }

}