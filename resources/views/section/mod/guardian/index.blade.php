@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div id="vueapp" class="content">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="card-box">
                            <h4 class="m-t-0 header-title">Messages à traiter</h4>
                            <div class="container">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>Joueur</th>
                                        <th>Date</th>
                                        <th>Message</th>
                                        <th>Sanction</th>
                                        <th>Actions</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        @foreach($data as $msg)
                                            <tr>
                                                <td>{{ $msg['playerName'] }}</td>
                                                <td>{{ $msg['date'] }}</td>
                                                <td style="max-width: 300px !important; overflow: hidden">{!! \App\Http\Controllers\moderation\GuardianController::Osiris($msg['message'], $msg['playerName'])['msg'] !!}</td>
                                                <td>{{ \App\Http\Controllers\moderation\GuardianController::Osiris($msg['message'], $msg['playerName'])['sanction'] }}</td>
                                                <td>Valider</td>
                                            </tr>
                                        @endforeach
                                    </tbody>
                                </table>
                                <div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
