import random


class Card:
    def __init__(self, color, value, real_value):
        self.color = color
        self.value = value
        self.real_value = real_value

    def __str__(self):   #用来将实例对象转换成 str，制作一张扑克牌
        return '{}{}:{}'.format(self.color, self.value, self.real_value)


class Game:
    def __init__(self):
        self.all_card = []
        self.three_card = []

    def init_card(self):
        values = list(range(2, 11)) + ['J', 'Q', 'K', 'A']   #list(range()) 循环遍历成列表 
        #循环嵌套 制作整副扑克牌
        for c in '????':
            count = 1             #count 表示序号，真实值
            for s in values: 
                card = Card(c, s, count)
                self.all_card.append(card)
                count += 1

    def get_card(self):
        for i in range(3):
            index = random.randint(0, len(self.all_card) - 1)   #random.randint()随机选出一个数字
            card = self.all_card.pop(index)       #   列表.pop()表示删除列表中指定的元素  
            self.three_card.append(card)

    def vali_card(self):


        # 先排序，以方便后面判断牌型
        self.three_card.sort(key=self.comple_card)     # 这里用到了 sort() 和后面的def comple_card() 函数构成以real_value来排序。
        c1 = self.three_card[0]
        c2 = self.three_card[1]
        c3 = self.three_card[2]

        if c1.real_value == c2.real_value == c3.real_value:
            print('三条')
        elif c1.real_value == c2.real_value or c1.real_value == c3.real_value or c2.real_value == c3.real_value:
            print('一对')
        elif c1.real_value + 1 == c2.real_value and c1.real_value + 2 == c3.real_value:
            print('顺子')
        elif c1.color == c2.color == c3.color:
            print('同花')
        elif c1.color == c2.color == c3.color and (
                c1.real_value + 1 == c2.real_value and c1.real_value + 2 == c3.real_value):
            print('同花顺')
        else:
            print('散牌')

    def comple_card(self, c):
        return c.real_value

    def test_card(self):
        for c in self.three_card:
            print(c)


g = Game()

g.init_card()
g.get_card()
g.vali_card()
g.test_card()
# g.test_card()
# g.vali_card()

