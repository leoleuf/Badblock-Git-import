@extends('layouts.app')
@section('content')
    <div class="row">
        <div class="col-12">
            <div class="card-box">
                <h4 class="header-title mt-0 m-b-30">Server : Bungee</h4>
                <div class="">
                    @foreach($data as $val)
                        <h5>Maintenance : @if($val['maintenance']['state']) <strong class="text text-success">Actif</strong> @else <strong class="text text-danger">Désactivé</strong> @endif</h5>
                        <pre class='code code-html'><label>MOTD</label><code><input value="{{ $val['description'] }}"></code></pre>
                        <h5>Joueurs en ligne : <strong>{{ $val['players']['online'] }}/{{ $val['players']['max'] }}</strong></h5>
                    @endforeach
                </div>
                <a href="/" class="btn btn-icon btn-warning"> <i class="fa fa-wrench"></i> </a>
            </div>
        </div>
    </div>
@endsection