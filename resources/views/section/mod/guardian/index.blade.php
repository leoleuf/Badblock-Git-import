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
                                    <tbody id="body">

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
@section('after_scripts')
<script>

    function getMsg(){
        $.ajax({

            url: '/api/get-msg-guardianner',
            type: 'GET',
            data: {},
            success: function(data) {
                $('#body').html(data);
            }

        });
    }

    getMsg();

    setInterval(getMsg, 3000);

</script>
@endsection