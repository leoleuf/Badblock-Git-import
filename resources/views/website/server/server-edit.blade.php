@extends('layouts.app')
<link href="/assets/plugins/switchery/switchery.min.css" rel="stylesheet" />

@section('content')
    <br>
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
    <div class="row">
        <div class="col-sm-12">
            <!-- will be used to show any messages -->
            @if (Session::has('message'))
                <div class="alert alert-info">{{ Session::get('message') }}</div>
            @endif
            <div class="panel">
                <div class="panel-body">
                    <h1>Edition du serveur "{{ $server['name'] }}"</h1>



                    <div class="">
                        {{ Form::model($server, array('url' => "/website/crud/server/" . $server['id'], 'method' => 'PUT','class'=>'form_inline')) }}
                        Nom du serveur :
                        {{ Form::text('name', $server['name'], array('class' => 'form-control','placeholder'=>"Nom d'affichage du Serveur")) }}
                        <br>
                        Nom réel du serveur :
                        {{ Form::text('realname', $server['realName'], array('class' => 'form-control','placeholder'=>"Nom réel du Serveur")) }}
                        <br>
                        Activer la visibilitée du serveur sur le site :
                        <br>
                        <br>
                        <input name ="visibility" type="checkbox"
                               @if($server['visibility'])
                                checked
                               @endif
                        data-plugin="switchery" data-color="#00b19d"/>

                        <br>
                        <br>

                        <button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-saved"></span> Save</button>

                        {{ Form::close() }}

                        {{ Form::open(array('url' => 'website/crud/server/' . $server['id'])) }}
                        {{ Form::hidden('_method', 'DELETE') }}
                        {{ Form::submit('Delete', array('class' => 'btn btn-danger')) }}
                        {{ Form::close() }}
                    </div>
                </div>
            </div>
        </div>
    </div>
    </div>
    </div>
    </div>


@endsection

<script src="/assets/plugins/switchery/switchery.min.js"></script>

