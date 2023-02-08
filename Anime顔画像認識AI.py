import cv2#openCV必須

#https://pystyle.info/opencv-cascade-classifier/ 理論
face_classifier = cv2.CascadeClassifier("c:/Users/siroK321/Documents/pythonfiles/pythondata/opencv-4.7.0/opencv-4.7.0/data/haarcascades/lbpcascade_animeface.xml")
#https://ultraist.hatenablog.com/entry/20110718/1310965532 学習用xmlファイルの場所
#https://github.com/nagadomi/lbpcascade_animeface/blob/master/lbpcascade_animeface.xml xmlファイルの場所サブ

# 画像ファイルを読み込む
img = cv2.imread("c:/Users/siroK321/Documents/pythonfiles/VBS_face.png")

# グレイスケールに変換,後述のdetectMultiScaleがグレーを要求するため
img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
 
# 読み込んだ画像から顔の取得
faces = face_classifier.detectMultiScale(img_gray)
 
# 検出した顔に対する処理
for (x, y, w, h) in faces:
    # 顔周辺に緑の四角を描画する
    cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)
 
# 指定したパスへ作成した画像を出力
cv2.imwrite("c:/Users/siroK321/Documents/pythonfiles/Person_animeface.png", img)
