# miNEMcraft
A plugin for bukkit/spigot.

[ ![Codeship Status for planethouki/miNEMcraft](https://app.codeship.com/projects/1f434340-b5e4-0135-02bd-0abdbc6c21b9/status?branch=master)](https://app.codeship.com/projects/258518)

## 進捗
* ハーベスト、マイニング仮完成

## やりたいこと
* ハーベスト 作物を収穫するとXEM（もしくはモザイク）がもらえる
* マイニング 地下を掘ると（ry
* アポスティーユ 飼いならしたモブの所有権をごにょごにょ

## アーキテクチャ的な
* testnet
* 同じサーバー上でspigotとNISを動かす
* やりとりは全てチェーンに乗せる
* 与えられるXEMはすべて鯖主が用意する

## コマンド
* /mnc balance 残高を確認する
* /mnc resister <Address> アドレスを登録する（上書き）
* /mnc generate アドレスを生成する。

## いちおう
### NISあげ
```
cd ~
wget https://bob.nem.ninja/nis-0.6.93.tgz
tar xzf nis-0.6.93.tgz
mv package/ nemServer/
cd nemServer/
./nix.runNis.sh
```
### 疑問
* ymlに秘密鍵かくと、他pluginからも参照できちゃう？