import random


class Card:
    def __init__(self, color, value, real_value):
        self.color = color
        self.value = value
        self.real_value = real_value

    def __str__(self):   #������ʵ������ת���� str������һ���˿���
        return '{}{}:{}'.format(self.color, self.value, self.real_value)


class Game:
    def __init__(self):
        self.all_card = []
        self.three_card = []

    def init_card(self):
        values = list(range(2, 11)) + ['J', 'Q', 'K', 'A']   #list(range()) ѭ���������б� 
        #ѭ��Ƕ�� ���������˿���
        for c in '????':
            count = 1             #count ��ʾ��ţ���ʵֵ
            for s in values: 
                card = Card(c, s, count)
                self.all_card.append(card)
                count += 1

    def get_card(self):
        for i in range(3):
            index = random.randint(0, len(self.all_card) - 1)   #random.randint()���ѡ��һ������
            card = self.all_card.pop(index)       #   �б�.pop()��ʾɾ���б���ָ����Ԫ��  
            self.three_card.append(card)

    def vali_card(self):


        # �������Է�������ж�����
        self.three_card.sort(key=self.comple_card)     # �����õ��� sort() �ͺ����def comple_card() ����������real_value������
        c1 = self.three_card[0]
        c2 = self.three_card[1]
        c3 = self.three_card[2]

        if c1.real_value == c2.real_value == c3.real_value:
            print('����')
        elif c1.real_value == c2.real_value or c1.real_value == c3.real_value or c2.real_value == c3.real_value:
            print('һ��')
        elif c1.real_value + 1 == c2.real_value and c1.real_value + 2 == c3.real_value:
            print('˳��')
        elif c1.color == c2.color == c3.color:
            print('ͬ��')
        elif c1.color == c2.color == c3.color and (
                c1.real_value + 1 == c2.real_value and c1.real_value + 2 == c3.real_value):
            print('ͬ��˳')
        else:
            print('ɢ��')

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

