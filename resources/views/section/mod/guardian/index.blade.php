@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <div id="vueapp" class="content">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="card-box">
                            <h4 class="m-t-0 header-title">Messages à traiter</h4>
                            <div class="card-box">
                                <div class="container">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Joueur</th>
                                                <th>Date</th>
                                                <th>Message</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <template v-if="messages.length > 0">
                                            <tr v-for="message in messages" v-if="!message.processed">
                                                <td>@{{ message.playerName }}</td>
                                                <td>@{{ message.date }}</td>
                                                <td>@{{ message.message }}</td>
                                                <td>
                                                    <div class="row">
                                                        <button :disabled="disabledButtonsMessages.includes(message)"
                                                                @click.prevent="messageMute(message)"
                                                                class="btn btn-warning">Mute</button>
                                                        <button :disabled="disabledButtonsMessages.includes(message)"
                                                                @click.prevent="messageBan(message)"
                                                                class="btn btn-danger">Ban</button>
                                                        <button :disabled="disabledButtonsMessages.includes(message)"
                                                                @click.prevent="messageOk(message)"
                                                                class="btn btn-success">OK</button>
                                                    </div>
                                                </td>
                                            </tr>
                                            </template>
                                        </tbody>
                                    </table>
                                    <div>
                                        <img v-if="loading"
                                             src="/vendor/backpack/tinymce/skins/charcoal/img/loader.gif"
                                             alt="loading">
                                    </div>
                                    <button class="btn btn-primary"
                                            @click.prevent="loadMore">Charger plus</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Mute duration modal -->
            <div class="modal" tabindex="-1" role="dialog" id="muteDurationModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Mute</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="muteDurationInput">Durée du mute</label>
                                <select name="muteDurationInput" class="form-control" id="muteDurationInput" v-model="muteDuration">
                                    <option v-for="muteDuration in getMuteDurations()" :value="muteDuration">@{{ muteDuration }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal" id="muteModalClosed">Fermer</button>
                            <button type="button" class="btn btn-primary" id="muteModalOk">Muter</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Ban duration modal -->
            <div class="modal" tabindex="-1" role="dialog" id="banDurationModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Ban</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="banDurationInput">Durée du ban</label>
                                <select name="banDurationInput" class="form-control" id="banDurationInput" v-model="banDuration">
                                    <option v-for="banDuration in getBanDurations()" :value="banDuration">@{{ banDuration }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal" id="banModalClosed">Fermer</button>
                            <button type="button" class="btn btn-primary" id="banModalOk">Bannir</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')
    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="/assets/plugins/toastr/toastr.min.js"></script>
    <script>
        const app = new Vue({
            el: '#vueapp',
            data: {
                messages: [],
                lastTimestamp: 0,
                disabledButtonsMessages: [],
                loading: true,
                muteDuration: '',
                banDuration: ''
            },
            methods: {
                loadUnprocessedMessage: async function(startTimestamp, limit = 50) {
                    this.loading = true;
                    const response = await axios.get('/moderation/ajax/unprocessed-messages?startTimestamp=' + startTimestamp + '&limit=' + limit);
                    if(response && response.statusText === 'OK') {
                        this.messages = this.messages.concat(response.data);
                        this.lastTimestamp = this.messages[this.messages.length - 1].timestamp;
                    }
                    this.loading = false;
                },
                loadMore: function() {
                    this.loadUnprocessedMessage(this.lastTimestamp)
                },
                messageOk: async function(message) {
                    this.disabledButtonsMessages.push(message);
                    const messageId = message._id.$oid;
                    await axios.post('/moderation/ajax/set-message-ok/' + messageId);
                    message.processed = true;
                    toastr.success('Message traité !');
                },
                messageMute: async function(message) {
                    this.disabledButtonsMessages.push(message);
                    const ok = await this.showMuteDialog();
                    if(ok) {
                        const messageId = message._id.$oid;
                        await axios.post('/moderation/ajax/mute-message-sender/' + messageId + '/' + this.muteDuration);
                        message.processed = true;
                        toastr.success('Utilisateur muté !');
                    } else {
                        // re-enable button
                        this.disabledButtonsMessages.splice(this.disabledButtonsMessages.indexOf(message), 1);
                    }
                },
                messageBan: async function(message) {
                    this.disabledButtonsMessages.push(message);

                    // toastr.success('Message traité !');
                    const ok = await this.showBanDialog();
                    if(ok) {
                        const messageId = message._id.$oid;
                        await axios.post('/moderation/ajax/ban-message-sender/' + messageId + '/' + this.banDuration);
                        message.processed = true;
                        toastr.success('Utilisateur banni !');
                    } else {
                        // re-enable button
                        this.disabledButtonsMessages.splice(this.disabledButtonsMessages.indexOf(message), 1);
                    }
                },
                showMuteDialog: function() {
                  return new Promise(function(resolve, reject) {
                      $('#muteDurationModal').modal({
                          backdrop: 'static',
                          keyboard: false
                      });
                      $('#muteModalOk').click(function(e) {
                          $('#muteDurationModal').modal('hide');
                          resolve(true);
                      });
                      $('#muteModalClosed').click(function(e) {
                          resolve(false);
                      });
                  });
                },
                showBanDialog: function() {
                    return new Promise(function(resolve, reject) {
                        $('#banDurationModal').modal({
                            backdrop: 'static',
                            keyboard: false
                        });
                        $('#banModalOk').click(function(e) {
                            $('#banDurationModal').modal('hide');
                            resolve(true);
                        });
                        $('#banModalClosed').click(function(e) {
                            resolve(false);
                        });
                    });
                },
                getMuteDurations: function() {
                    return [
                        '1 heure',
                        '3 heures',
                        '6 heures',
                        '12 heures',
                        '24 heures',
                        '3 jours',
                        '7 jours',
                        '15 jours',
                        '30 jours',
                        '60 jours',
                    ]
                },
                getBanDurations : function() {
                    return [
                        '1 heure',
                        '1 jour',
                        '3 jours',
                        '7 jours',
                    ]
                }
            },
            mounted() {
                this.loadUnprocessedMessage(this.lastTimestamp);
            }
        });
    </script>
@endsection
