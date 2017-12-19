Alfresco Process Services 引き戻しサンプル
====

Overview
Alfresco Process Services（以下APS）の引き戻しサンプルのワークフローとテストコード

## Description
APSで引き戻しを行う場合、戻したいタスクの前にRecieve Taskを入れて、そこにシグナルを飛ばす。
サンプルでは、以下のようなワークフローを想定している。

## wf
![wf](https://www.rsapp.jp/private/stash/users/aochi.yoshihiko/repos/aps-recall-sample/browse/images/sample-workflow.png)

AppTestの中で、userTask2まで進んだタスクをreceiveTaskに戻して、そこにシグナルを送ることで、後続のuserTask1を実行している。

## Usage
AppTestの中に実体のコードが。引き戻す場合には、この中のmoveToTask()を参考にする。

## Note
APSから直接ボタンを設けてシグナルを飛ばすことはできなさそうなので、ADFを使って実装しないといけない（現状）
