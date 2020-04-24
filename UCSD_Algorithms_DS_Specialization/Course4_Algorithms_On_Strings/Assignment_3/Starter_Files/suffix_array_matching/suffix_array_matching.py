# python3
import sys

def sort_doubled(text, subStrLen, order, classes):
    len_text = len(text)
    count = [0] * len_text
    new_order = [0] * len_text

    for i in range(len_text):
        count[classes[i]] += 1
    for j in range(1, len_text):
        count[j] += count[j - 1]

    for i in range(len_text - 1, -1, -1):
        start = (order[i] - subStrLen + len_text) % len_text
        cl = classes[start]
        count[cl] -= 1
        new_order[count[cl]] = start

    return new_order


def update_classes(new_order, clss, l):
    n = len(new_order)
    new_clss = [0] * n

    for i in range(1, n):
        cur, prev = new_order[i], new_order[i - 1]
        mid, mid_prev = cur + l, (prev + l) % n
        if clss[cur] != clss[prev] or clss[mid] != clss[mid_prev]:
            new_clss[cur] = new_clss[prev] + 1
        else:
            new_clss[cur] = new_clss[prev]

    return new_clss


def build_suffix_array(text):
    order = [0] * len(text)
    char_set = sorted(set(text))
    count = [text.count(c) for c in char_set]

    for i in range(1, len(count)):
        count[i] += count[i - 1]

    for i, c in reversed(list(enumerate(text))):
        count[char_set.index(c)] -= 1
        order[count[char_set.index(c)]] = i

    classes = [0] * len(text)

    for i in range(1, len(text)):
        if text[order[i]] != text[order[i - 1]]:
            classes[order[i]] = classes[order[i - 1]] + 1
        else:
            classes[order[i]] = classes[order[i - 1]]

    subStrLen = 1
    lengthOfText = len(text)

    while subStrLen < lengthOfText:
        order = sort_doubled(text, subStrLen, order, classes)
        classes = update_classes(order, classes, subStrLen)
        subStrLen = subStrLen * 2

    return order
	
def find_occurrences(text, patterns):
    occs = set()

    suffixArr = build_suffix_array(text)
    // write your code here

    return occs

if __name__ == '__main__':
    text = sys.stdin.readline().strip()
    pattern_count = int(sys.stdin.readline().strip())
    patterns = sys.stdin.readline().strip().split()
    occs = find_occurrences(text, patterns)
    print(" ".join(map(str, occs)))