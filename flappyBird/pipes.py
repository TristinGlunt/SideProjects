import pygame
import random

class Pipe(pygame.sprite.Sprite):
    def __init__(self, x, height):
        pygame.sprite.Sprite.__init__(self)
        self.x = x
        self.height = height
        ranNum = random.uniform(0.1, 0.9)
        space = 100 #100 * ranNum
        self.topY = 300 * ranNum
        self.bottomY = self.topY + space
        self._lessThanX = False

    def getX(self):
        return self.x

    def getXRight(self):
        return self.x + 50

    def getY(self):
        return self.y

    def getTopY(self):
        return self.topY

    def getBottomY(self):
        return self.bottomY

    def setX(self, x):
        self.x = x

    @property
    def lessThanX(self):
        return self._lessThanX

    @lessThanX.setter
    def lessThanX(self, bool):
        self._lessThanX = bool

    @lessThanX.deleter
    def lessThanX(self):
        del self._lessThanX

    def hits(self, bird):
        if(bird.rect.y < self.topY or bird.rect.y > self.bottomY):
            if(bird.rect.x > self.x and bird.rect.x < self.x + 50): #TODO UPDATE WITH PARAM
                return True
