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
                                            @foreach($Sanctions as $row)
                                                <tr class="gradeX">
                                                    <td>{{ $row['punisher'] }}</td>
                                                    <td>{{ $row['reason'] }}</td>
                                                    <td>{{ date('Y-m-d H:i:s', round(($row['expire'] / 1000), 0)) }}</td>
                                                    <td>
                                                        {{ Form::open() }}
                                                            <input type="button" id="notif" value="Notifier" onclick="" class="btn btn-info" />
                                                        {{ Form::close() }}
                                                    </td>
                                                </tr>
                                            @endforeach
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

    $(function(){

        $('#notif').on('click', function(){

            $.ajax({

                type: 'POST',

                url: ''
            });

        });

    });

</script>

@endsection