@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h1>Sanction sans preuves :</h1>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="panel">
                            <div class="panel-body">
                                    <div class="">
                                        <table class="table table-striped" id="datatable-editable">
                                            <thead>
                                            <tr>
                                                <th>Banner</th>
                                                <th>Raison</th>
                                                <th>Temps</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            @for($i = 0; $i < count($Sanctions); $i++)
                                                <tr class="gradeX">
                                                    <td>{{ $Sanctions[$i]['punisher'] }}</td>
                                                    <td>{{ $Sanctions[$i]['reason'] }}</td>
                                                    <td>{{ date('Y-m-d H:i:s', round(($Sanctions[$i]['expire'] / 1000), 0)) }}</td>
                                                    <td>
                                                        {{ Form::open() }}
                                                            <input type="button" id="notif{{$i}}" value="Notifier" onclick="notif('{{ $Sanctions[$i]['punisher']  }}');" class="btn btn-info" />
                                                        {{ Form::close() }}
                                                    </td>
                                                </tr>
                                            @endfor
                                            </tbody>
                                        </table>
                                    </div>
</div>
<!-- end: panel body -->

</div> <!-- end panel -->
</div> <!-- end col-->
</div>
<!-- end row -->

</div>
</div>
</div>
@endsection
@section("after_scripts")


    <script src="/assets/plugins/toastr/toastr.min.js"></script>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="/assets/jquery.json-editor.min.js"></script>

<script>

    function notif(punisher) {
        $.ajax({

            type: 'POST',

            url: '/section/preuves',

            data: {
                'punisher': punisher
            },

            success: function(data)
            {
                toastr.success('La personne a bien été notifiée', 'Merci !');
                console.log('Notifié !');
            },
            error: function(data)
            {
                toastr.error('Erreur de l\'envoi !', 'Erreur !');
                console.log('Erreur !');
            }
        });
    }

</script>

@endsection