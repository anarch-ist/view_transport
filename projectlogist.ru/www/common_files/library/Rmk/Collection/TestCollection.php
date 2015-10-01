<?php
/**
 * Реализация интерфейса Rmk\Collection\Collection для тестирования.
 *
 * @category Rmk
 * @package  Rmk_Collection
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Collection;

use Rmk\Collection\Collection,
    Rmk\Util\ArrayHelper,
    ArrayIterator;

class TestCollection implements Collection
{

    /**
     * Массив.
     *
     * @var array
     */
    protected $array = array();

    /**
     * Конструктор.
     *
     * @param  array $from = null
     * @return void
     */
    public function __construct(array $from = null)
    {
        if (null !== $from) {
            $this->fromArray($from);
        }
    }

    /**
     * Возвращает значение в указанной позиции.
     *
     * @param  int $position
     * @return mixed
     */
    public function getAt($position)
    {
        return ArrayHelper::getAt($this->array, $position);
    }

    /**
     * Возвращает первое значение.
     *
     * @return mixed
     */
    public function getFirst()
    {
        return ArrayHelper::getFirst($this->array);
    }

    /**
     * Возвращает последнее значение.
     *
     * @return mixed
     */
    public function getLast()
    {
        return ArrayHelper::getLast($this->array);
    }

    /**
     * Добавляет значение в коллекцию.
     *
     * @param  mixed $value
     * @return Collection
     */
    public function add($value)
    {
        ArrayHelper::add($this->array, $value);

        return $this;
    }

    /**
     * Проверяет, содержит ли коллекция значение.
     *
     * @param  mixed $value
     * @return boolean
     */
    public function contains($value)
    {
        return ArrayHelper::containsValue($this->array, $value);
    }

    /**
     * Проверяет, содержит ли коллекция переданную коллекцию.
     *
     * @param  Collection $collection
     * @return boolean
     */
    public function containsCollection(Collection $collection)
    {
        if ($collection instanceof AbstractCollection) {
            return ArrayHelper::containsValues(
                            $this->array, $collection->array
            );
        }

        $me       = $this;
        $contains = true;
        $collection->each(function($value) use ($contains, $me) {
                    return $contains = $me->contains($value);
                }
        );

        return $contains;
    }

    /**
     * Проверяет, одинаковые ли коллекции.
     *
     * @param  Collection $collection
     * @return boolean
     */
    public function equals(Collection $collection)
    {
        if ($collection instanceof AbstractCollection) {
            return ArrayHelper::equalsValues(
                            $this->array, $collection->array
            );
        }

        if ($this->count() !== $collection->count()) {
            return false;
        }

        $me     = $this;
        $equals = true;
        $collection->each(function($value) use (&$equals, $me) {
                    return $equals = $me->contains($value);
                }
        );

        return $equals;
    }

    /**
     * Проверяет, пустая ли коллекция.
     *
     * @return boolean
     */
    public function isEmpty()
    {
        return empty($this->array);
    }

    /**
     * Проверяет, не пустая ли коллекция.
     *
     * @return boolean
     */
    public function isNotEmpty()
    {
        return !empty($this->array);
    }

    /**
     * Удаляет значение из коллекции.
     *
     * @param  mixed $value
     * @return Collection
     */
    public function remove($value)
    {
        ArrayHelper::removeValue($this->array, $value);

        return $this;
    }

    /**
     * Удаляет значение в указанной позиции.
     *
     * @param  int $position
     * @return Collection
     */
    public function removeAt($position)
    {
        ArrayHelper::removeAt($this->array, $position);

        return $this;
    }

    /**
     * Удаляет первое значение.
     *
     * @return Collection
     */
    public function removeFirst()
    {
        ArrayHelper::removeFirst($this->array);

        return $this;
    }

    /**
     * Удаляет последнее значение.
     *
     * @return Collection
     */
    public function removeLast()
    {
        ArrayHelper::removeLast($this->array);

        return $this;
    }

    /**
     * Удаляет коллекцию из коллекции.
     *
     * @param  Collection $collection
     * @return Collection
     */
    public function removeCollection(Collection $collection)
    {
        if ($collection instanceof AbstractCollection) {
            return ArrayHelper::removeArrayValues(
                            $this->array, $collection->array
            );
        }

        $me = $this;
        $collection->each(function($value) use($me) {
                    $me->remove($value);
                }
        );

        return $this;
    }

    /**
     * Очищает коллекцию.
     *
     * @return Collection
     */
    public function clear()
    {
        ArrayHelper::clear($this->array);

        return $this;
    }

    /**
     * Преобразовывает коллекцию в массив.
     *
     * @return array
     */
    public function toArray()
    {
        return $this->array;
    }

    /**
     * Выполняет переданную функцию для каждого элемента коллекции.
     *
     * Выполняет переданную функцию для каждого элемента коллекции. Если функция
     * возвращает false, то дальнейшая обработка прекращается.
     *
     * @param  callback $callback
     * @return boolean
     */
    public function each($callback)
    {
        return ArrayHelper::each($this->array, $callback);
    }

    /**
     * Выполняет переданную функцию для всех элементов коллекции.
     *
     * @param  callback $callback
     * @return Collection
     */
    public function every($callback)
    {
        ArrayHelper::every($this->array, $callback);

        return $this;
    }

    /**
     * Возвращает количество объектов в коллекции.
     *
     * @return int
     */
    public function count()
    {
        return count($this->array);
    }

    /**
     * Возвращает новый итератор коллекции.
     *
     * @return ArrayIterator
     */
    public function getIterator()
    {
        return new ArrayIterator($this->array);
    }

    /**
     * Заполняет коллекцию данными массива.
     *
     * @param  array $array
     * @return Collection
     */
    public function fromArray(array $array)
    {
        $this->array = $array;

        return $this;
    }

    /**
     * Фильтрует коллекцию с помощью функции фильтрации.
     *
     * @param  callback $filter
     * @return Filterable
     */
    public function filterBy($filter)
    {
        ArrayHelper::filterBy($this->array, $filter);

        return $this;
    }

    /**
     * Фильтрует пустые значения коллекции.
     *
     * @return Filterable
     */
    public function filter()
    {
        ArrayHelper::filter($this->array);

        return $this;
    }

    /**
     * Сортирует коллекцию с помощью функции сортировки.
     *
     * @param  callback $sorter
     * @return Sortable
     */
    public function sortBy($sorter)
    {
        ArrayHelper::sortAsHashBy($this->array, $sorter);

        return $this;
    }

    /**
     * Сортирует коллекцию по возрастанию.
     *
     * @return Sortable
     */
    public function sortByAscending()
    {
        ArrayHelper::sortAsHashByAscending($this->array);

        return $this;
    }

    /**
     * Сортирует коллекцию по убыванию.
     *
     * @return Sortable
     */
    public function sortByDescending()
    {
        ArrayHelper::sortAsHashByDescending($this->array);

        return $this;
    }

}