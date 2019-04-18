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
                                            @php $content = \App\Http\Controllers\moderation\GuardianController::Osiris($msg['message'], $msg['playerName']) @endphp
                                            <tr>
                                                <td>{{ $msg['playerName'] }}</td>
                                                <td>{{ $msg['date'] }}</td>
                                                <td style="max-width: 300px !important; overflow: hidden">{!! $content['msg'] !!}</td>
                                                <td>{{ $content['sanction'] }}</td>
                                                <td><a href="/api/msg-del-guardianner/{{ $msg['_id'] }}" class="btn btn-success" style="margin-right: 10px"><i class="fas fa-check"></i></a><a href="/api/msg-guardianner/{{ $msg['_id'] }}" class="btn btn-danger"><i class="fas fa-gavel"></i></a></td>
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
