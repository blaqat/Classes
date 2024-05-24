"""
CSAPX Lab 3: Merchants of Venice
Given a list of merchants at integer locations on a road, find the optimal
location to place a new merchant, such that the sum of the distances the
new merchant must travel to all other merchants is minimized.
$ python3 merchants.py [slow|fast] input-file
Author: Sean Strout @ RIT CS
Author: Aiden Green
"""

from dataclasses import dataclass
import sys  # arg
import time  # clock
import random  # random

from typing import List  # List
import rit_sort as sorts


@dataclass
class Merchant:
    name: str
    location: int

    # instead of changing _partition or quick_sort to comparing with data[n].location,
    # I opted for just adding the less than metamethod
    def __lt__(self, other):
        return self.location - other.location < 0

    def __cmp__(self, other):
        return abs(self.location - other.location)

    def getSumOfDistances(self, others: list) -> int:
        """
        Gets the sum of the distances from merchants to this merchant
        :param others: List of merchants
        :return: int, the sum of the distances from the merchant to the merchants in the list
        """
        s = 0

        for merchant in others:
            if merchant != self:
                s += self.__cmp__(merchant)

        return s


def quick_select(data: List[int]) -> int:
    """
    Performs a quick select on a list
    :param data: The data to find element from
    :return: (len(data) // 2)-th smallest element in list
    """
    if len(data) > 0:
        k = len(data) // 2
        pivot = data[random.randint(0, len(data)-1)]
        smaller, equal, larger = sorts._partition(data, pivot)
        m, count = len(smaller), len(equal)
        return m <= k < m+count and pivot or m > k and quick_select(smaller) or quick_select(larger)
    else:
        return []


def readMerchants(fileName: str) -> List[Merchant]:
    """
    Reads and converts merchants listed in a file into Merchant class
    and returns a List of created Merchants
    :param fileName:
    :return: list
    """
    merchants: List[Merchant] = []

    with open(fileName) as file:
        for merchant in file:
            args = merchant.split(" ")
            merchants.append(Merchant(args[0], int(args[1])))

    return merchants


def main() -> None:
    """
    The main function.
    :return: None
    """
    start = time.perf_counter()
    slow = sys.argv[1] == "slow"
    merchants = readMerchants(sys.argv[2])
    merchant: Merchant

    if slow:
        merchants = sorts.quick_sort(merchants)
        merchant = merchants[len(merchants) // 2]
    else:
        merchant = quick_select(merchants)

    print("Search type: ", slow and "slow" or "fast")
    print("Number of merchants: ", len(merchants))
    print("Elapsed time: ", time.perf_counter() - start)
    print("Optimal store location: ", merchant)
    print("Sum of distances: ", merchant.getSumOfDistances(merchants))




if __name__ == '__main__':
    main()
