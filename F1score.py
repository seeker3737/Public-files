#評価関数
#F1スコア
"""
pretext = 'I like sushi so I love Japan.'
preteacher_text = 'I like apples so I went to Aomori.'
"""
pretext = '首相 が アメリカ を 訪問 し た .'
preteacher_text = '山田 首相 は 23 日 アメリカ を 訪問 し まし た.'


#recall定義
def recall(text1,text2):
  i=0
  j=0
  count=0
  text1=text1.replace('.',' ')
  text1=text1.split()
  text2=text2.replace('.',' ')
  text2=text2.split()

  while(i < len(text1)):
    count += text2.count(text1[i])
    i += 1
  else:
    return count/len(text2)

#precision定義
def precision(text1,text2):
  i=0
  j=0
  count=0
  text1=text1.replace('.',' ')
  text1=text1.split()
  text2=text2.replace('.',' ')
  text2=text2.split()

  while(i < len(text1)):
    count += text2.count(text1[i])
    i += 1
  else:
    return count/len(text1)
