var sendmail = {

    send: function(successCallback, errorCallback, subject, body, sender, password, recipients, attachFiles){
        cordova.exec(successCallback,
            errorCallback,
            "SendMail",
            "send",
            [{
                 "subject":subject,
                 "body":body,
                 "sender":sender,
                 "password":password,
                 "recipients":recipients,
                 "attachFiles": attachFiles,
            }]
        );
    }
}

module.exports = sendmail;
