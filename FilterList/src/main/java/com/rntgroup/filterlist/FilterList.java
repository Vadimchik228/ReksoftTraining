package com.rntgroup.filterlist;

import java.util.*;

public class FilterList<E> extends AbstractList<E> implements List<E> {

    private final Collection<E> predicate;
    private final List<E> filteredList;

    public FilterList(Collection<E> objects, Collection<E> predicate) {
        this.predicate = predicate;
        this.filteredList = new ArrayList<>(objects);
    }

    @Override
    public int size() {
        return filteredList.size();
    }

    @Override
    public boolean isEmpty() {
        return filteredList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return filteredList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new FilterListIterator<>(filteredList.listIterator(), predicate);
    }

    @Override
    public Object[] toArray() {
        return filteredList.toArray();
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (predicate.contains(e)) {
            return false;
        } else {
            filteredList.add(e);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (predicate.contains(o)) {
            return false;
        }
        return filteredList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return filteredList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean added = false;
        for (E element : c) {
            if (!predicate.contains(element)) {
                added = true;
                filteredList.add(element);
            }
        }
        return added;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean added = false;
        for (E element : c) {
            if (!predicate.contains(element)) {
                added = true;
                filteredList.add(index, element);
                index++;
            }
        }
        return added;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (!predicate.contains(element)) {
                modified |= filteredList.remove(element);
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        filteredList.clear();
    }

    @Override
    public E get(int index) {
        return filteredList.get(index);
    }

    @Override
    public E set(int index, E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (predicate.contains(element)) {
            return null;
        } else {
            return filteredList.set(index, element);
        }
    }

    @Override
    public void add(int index, E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (!predicate.contains(element)) {
            filteredList.add(index, element);
        }
    }

    @Override
    public E remove(int index) {
        E element = filteredList.get(index);
        if (!predicate.contains(element)) {
            return filteredList.remove(index);
        }
        return null;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return filteredList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return filteredList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new FilterListIterator<>(filteredList.listIterator(), predicate);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new FilterListIterator<>(filteredList.listIterator(index), predicate);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return filteredList.subList(fromIndex, toIndex);
    }

    private static class FilterListIterator<E> implements ListIterator<E> {

        private final ListIterator<E> iterator;

        private final Collection<E> predicate;

        private E previousElement;

        private E nextElement;

        private int previousIndex;

        private int nextIndex;

        public FilterListIterator(ListIterator<E> iterator, Collection<E> predicate) {
            this.iterator = iterator;
            this.predicate = predicate;

            previousElement = null;
            previousIndex = -1;

            // Для того чтобы у итератора вызвать метода next() и перейти к следующему элементу, нужно
            // проверить наличие следующего доступного в списке элемента с помощью метода hasNext().
            // Для этого в конструкторе необходимо предварительно установить nextElement и nextIndex.
            setNext();
        }

        private void setPrevious() {
            // Осуществляется поиск previousElement, начиная с nextIndex двигаясь влево.
            previousElement = findPrevious(nextIndex);
            previousIndex = previousElement == null ? iterator.previousIndex() : iterator.nextIndex();
        }

        private void setNext() {
            // Осуществляется поиск nextElement, начиная с previousIndex двигаясь вправо.
            nextElement = findNext(previousIndex);
            nextIndex = nextElement == null ? iterator.nextIndex() : iterator.previousIndex();
        }

        // После нахождения nextElement итератор останавливается на нем. Поэтому для того, чтобы выполнить
        // методы set(), remove(), add(), которые соответственно изменяют previousElement,
        // удаляют previousElement и добавляют новый элемент справа от previousElement,
        // необходимо предварительно поставить курсор на previousElement.
        private void setCursor(int index) {
            while (iterator.previousIndex() > index) {
                iterator.previous();
            }
        }

        private E findNext(int startIndex) {
            // В случае, если после выполнения методов set(), remove() или add()
            // необходимо вызвать метод next() и получить новый nextElement,
            // а курсор указывает не на исходный nextElement, откуда нужно осуществлять поиск,
            // а на элемент левее, то курсор нужно сдвинуть вправо и установить на исходный nextElement.
            while (iterator.previousIndex() < startIndex) {
                iterator.next();
            }
            while (iterator.hasNext()) {
                E element = iterator.next();
                if (!predicate.contains(element)) {
                    return element;
                }
            }
            return null;
        }

        private E findPrevious(int startIndex) {
            // После выполнения метода remove() исходный previousElement удалился, а новое значение не установлено.
            // Курсор указывает на элемент, идущий следующим после исходного previousElement. Но это может быть
            // сам nextElement, поэтому курсор надо сдвинуть левее и осуществлять поиск previousElement оттуда.
            while (iterator.nextIndex() > startIndex) {
                iterator.previous();
            }
            while (iterator.hasPrevious()) {
                E element = iterator.previous();
                if (!predicate.contains(element)) {
                    return element;
                }
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return nextElement != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            E result = nextElement;

            previousElement = nextElement;
            previousIndex = nextIndex;
            // Ищем новый nextElement.
            setNext();

            return result;
        }

        @Override
        public boolean hasPrevious() {
            return previousElement != null;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            E result = previousElement;

            nextElement = previousElement;
            nextIndex = previousIndex;
            // Ищем новый previousElement
            setPrevious();

            return result;
        }

        @Override
        public int nextIndex() {
            // Для работы теста iteratorBehaviourInLastPosition, где вызывается iterator.nextIndex().
            return nextElement != null ? nextIndex : iterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return previousElement != null ? previousIndex : -1;
        }

        @Override
        public void remove() {
            // После того как установили значение nextElement, курсор указывает на него.
            // А удалить необходимо previousElement. Поэтому сдвигаем курсор на previousElement.
            setCursor(previousIndex);
            if (!predicate.contains(iterator.previous())) {
                iterator.remove();
                // После удаления исходного previousElement нужно найти новый previousElement
                setPrevious();
                // А потом передвинуть курсор на nextElement и пересчитать его индекс.
                setNext();
            }
        }

        @Override
        public void set(E e) {
            if (!predicate.contains(e)) {
                setCursor(previousIndex);
                iterator.previous();
                iterator.set(e);
                previousElement = e;
                iterator.next();
            }
        }

        @Override
        public void add(E e) {
            if (!predicate.contains(e)) {
                // Для выполнения теста addByIterator(), где новый элемент e добавляется справа от
                // previousElement, но необходимо пропустить те элементы, которые содержатся в предикате,
                // поэтому сдвигаем курсор на позицию перед nextElement.
                setCursor(nextIndex - 1);

                iterator.add(e);

                // Добавленный элемент становится previousElement
                previousElement = e;
                previousIndex = iterator.previousIndex();

                // Необходимо передвинуть курсор на nextElement и пересчитать его индекс.
                setNext();
            }
        }
    }

}