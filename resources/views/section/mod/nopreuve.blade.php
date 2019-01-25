@extends('layouts.app')
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
                                                            <input type="button" id="notif{{$i}}" value="Notifier" onclick="notif();" class="btn btn-info" />
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

<script>

    function notif() {
        $.ajax({

            type: 'POST',

            url: '/section/preuves'
        });
    }

</script>

@endsection