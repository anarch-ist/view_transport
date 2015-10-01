<?php
/**
 * Автотест карты общего назначения.
 *
 * @category Rmk
 * @package  Rmk_Collection
 * @author   Roman rmk Mogilatov rmogilatov@gmail.com
 */

namespace RmkTest\Collection;

use Rmk\Collection\MixedMap,
    PHPUnit_Framework_TestCase;

include 'TestCollection.php';
class MixedMapTest extends PHPUnit_Framework_TestCase
{

    /**
     * Тестовые данные.
     *
     * @var array
     */
    protected $array;

    /**
     * Карта общего назначения.
     *
     * @var MixedMap
     */
    protected $map;

    public function setUp()
    {
        $this->map = new MixedMap();

        $this->array = array(
            'k1' => 'v1',
            'k2' => 'v2',
            'k3' => 'v3'
        );

        $this->map->fromArray($this->array);
    }

    public function tearDown()
    {
        $this->map->clear();
    }

    public function testConstruct()
    {
        new MixedMap();
    }

    public function testConstructFromArray()
    {
        $map = new MixedMap($this->array);

        $this->assertEquals($this->array, $map->toArray());
    }

    public function testGetAt()
    {
        $this->assertEquals('v1', $this->map->getAt(0));
        $this->assertEquals('v2', $this->map->getAt(1));
        $this->assertEquals('v3', $this->map->getAt(2));

        $this->setExpectedException('UnexpectedValueException');
        $this->map->getAt(25);
    }

    public function testGetFirst()
    {
        $this->assertEquals('v1', $this->map->getFirst());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->clear();

        $this->map->getFirst();
    }

    public function testGetLast()
    {
        $this->assertEquals('v3', $this->map->getLast());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->clear();

        $this->map->getLast();
    }

    public function testAdd()
    {
        $this->map
                ->add('v4')
                ->add('v5')
                ->add('v6');

        $this->assertEquals(6, $this->map->count());

        $this->assertEquals('v1', $this->map->getAt(0));
        $this->assertEquals('v2', $this->map->getAt(1));
        $this->assertEquals('v3', $this->map->getAt(2));
        $this->assertEquals('v4', $this->map->getAt(3));
        $this->assertEquals('v5', $this->map->getAt(4));
        $this->assertEquals('v6', $this->map->getAt(5));
    }

    public function testContains()
    {
        $this->assertTrue($this->map->contains('v1'));
        $this->assertTrue($this->map->contains('v2'));
        $this->assertTrue($this->map->contains('v3'));

        $this->assertFalse($this->map->contains('23'));
        $this->assertFalse($this->map->contains(-1));
        $this->assertFalse($this->map->contains('k1'));
        $this->assertFalse($this->map->contains('k2'));
        $this->assertFalse($this->map->contains('k3'));
    }

    public function testContainsCollection()
    {
        $anotherMap = new MixedMap($this->array);

        $this->assertTrue($this->map->containsCollection($anotherMap));

        $this->map->remove('v1');

        $this->assertFalse($this->map->containsCollection($anotherMap));

        $anotherMap->remove('v1');

        $this->assertTrue($this->map->containsCollection($anotherMap));

        $this->map->remove('v3');

        $this->assertFalse($this->map->containsCollection($anotherMap));
    }

    public function testContainsCollectionNotAbstractCollection()
    {
        $testCollection = new TestCollection($this->array);

        $this->assertTrue($this->map->containsCollection($testCollection));

        $this->map->remove('v1');

        $this->assertFalse($this->map->containsCollection($testCollection));

        $testCollection->remove('v1');

        $this->assertTrue($this->map->containsCollection($testCollection));

        $this->map->remove('v3');

        $this->assertFalse($this->map->containsCollection($testCollection));
    }

    public function testEquals()
    {
        $anotherMap = new MixedMap($this->array);

        $this->assertTrue($this->map->equals($anotherMap));

        $this->map->remove('v1');

        $this->assertFalse($this->map->equals($anotherMap));
    }

    public function testEqualsNotAbstractCollection()
    {
        $testCollection = new TestCollection($this->array);

        $this->assertTrue($this->map->equals($testCollection));

        $this->map->remove('v1');

        $this->assertFalse($this->map->equals($testCollection));
    }

    public function testIsEmpty()
    {
        $this->assertFalse($this->map->isEmpty());

        $this->map->clear();

        $this->assertTrue($this->map->isEmpty());
    }

    public function testIsNotEmpty()
    {
        $this->assertTrue($this->map->isNotEmpty());

        $this->map->clear();

        $this->assertFalse($this->map->isNotEmpty());
    }

    public function testRemove()
    {
        $this->map
                ->remove('v1')
                ->remove('v2')
                ->remove('v3');

        $this->assertTrue($this->map->isEmpty());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->remove('v1');
    }

    public function testRemoveAt()
    {
        $this->map
                ->removeAt(2)
                ->removeAt(1)
                ->removeAt(0);

        $this->assertTrue($this->map->isEmpty());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->removeAt(89);
    }

    public function testRemoveFirst()
    {
        $this->map
                ->removeFirst()
                ->removeFirst()
                ->removeFirst();

        $this->assertTrue($this->map->isEmpty());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->removeFirst();
    }

    public function testRemoveLast()
    {
        $this->map
                ->removeLast()
                ->removeLast()
                ->removeLast();

        $this->assertTrue($this->map->isEmpty());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->removeLast();
    }

    public function testRemoveCollection()
    {
        $anotherMap = new MixedMap($this->array);

        $this->map->removeCollection($anotherMap);

        $this->assertTrue($this->map->isEmpty());
    }

    public function testRemoveCollectionNotAbstractCollection()
    {
        $testCollection = new TestCollection($this->array);

        $this->map->removeCollection($testCollection);

        $this->assertTrue($this->map->isEmpty());
    }

    public function testClear()
    {
        $this->assertTrue($this->map->isNotEmpty());

        $this->map->clear();

        $this->assertTrue($this->map->isEmpty());
    }

    public function testToArray()
    {
        $this->assertEquals($this->array, $this->map->toArray());
    }

    public function testEach()
    {
        $i  = 0;
        $me = $this;

        $handler = function(&$value, $key) use ($me, &$i) {
                    $i++;
                    switch ($i) {
                        case 1:
                            $this->assertEquals('k1', $key);
                            $this->assertEquals('v1', $value);

                            $value = 'v11';
                            break;
                        case 2:
                            $this->assertEquals('k2', $key);
                            $this->assertEquals('v2', $value);

                            $value = 'v22';

                            return false;
                            break;
                        case 3:
                            $this->assertEquals('k3', $key);
                            $this->assertEquals('v3', $value);

                            $value = 'v33';
                            break;
                    }
                };

        $this->map->each($handler);

        $this->assertEquals('v11', $this->map->getAt(0));
        $this->assertEquals('v22', $this->map->getAt(1));
        $this->assertEquals('v3', $this->map->getAt(2));

        $this->assertEquals(2, $i);
    }

    public function testEvery()
    {
        $i  = 0;
        $me = $this;

        $handler = function(&$value, $key) use ($me, &$i) {
                    $i++;
                    switch ($i) {
                        case 1:
                            $this->assertEquals('k1', $key);
                            $this->assertEquals('v1', $value);

                            $value = 'v11';
                            break;
                        case 2:
                            $this->assertEquals('k2', $key);
                            $this->assertEquals('v2', $value);

                            $value = 'v22';

                            return false;
                            break;
                        case 3:
                            $this->assertEquals('k3', $key);
                            $this->assertEquals('v3', $value);

                            $value = 'v33';
                            break;
                    }
                };

        $this->map->every($handler);

        $this->assertEquals('v11', $this->map->getAt(0));
        $this->assertEquals('v22', $this->map->getAt(1));
        $this->assertEquals('v33', $this->map->getAt(2));

        $this->assertEquals(3, $i);
    }

    public function testCount()
    {
        $this->assertEquals(3, $this->map->count());
    }

    public function testGetIterator()
    {
        $i = 0;

        foreach ($this->map as $key => $value) {
            $i++;
            switch ($i) {
                case 1:
                    $this->assertEquals('k1', $key);
                    $this->assertEquals('v1', $value);

                    $value = 'v11';
                    break;
                case 2:
                    $this->assertEquals('k2', $key);
                    $this->assertEquals('v2', $value);

                    $value = 'v22';

                    break;
                case 3:
                    $this->assertEquals('k3', $key);
                    $this->assertEquals('v3', $value);

                    $value = 'v33';
                    break;
            }
        }

        $this->assertEquals('v1', $this->map->getAt(0));
        $this->assertEquals('v2', $this->map->getAt(1));
        $this->assertEquals('v3', $this->map->getAt(2));

        $this->assertEquals(3, $i);
    }

    public function testFilterBy()
    {
        $this->map->filterBy(
                function($value) {
                    return false;
                }
        );

        $this->assertTrue($this->map->isEmpty());
    }

    public function testFilter()
    {
        $nullValueMap = new MixedMap(
                        array(
                            'k1' => 0,
                            'k2' => 0.0,
                            'k3' => null,
                            'k4' => '',
                            'k5' => false,
                            'k6' => array()
                        )
        );

        $nullValueMap->filter();

        $this->assertTrue($nullValueMap->isEmpty());
    }

    public function testSortBy()
    {
        $this->map->sortBy(
                function($value1, $value2) {
                    return $value1 < $value2;
                }
        );

        $this->assertEquals('v3', $this->map->getAt(0));
        $this->assertEquals('v2', $this->map->getAt(1));
        $this->assertEquals('v1', $this->map->getAt(2));
    }

    public function testSortByAscending()
    {
        $this->map->sortByAscending();

        $this->assertEquals('v1', $this->map->getAt(0));
        $this->assertEquals('v2', $this->map->getAt(1));
        $this->assertEquals('v3', $this->map->getAt(2));
    }

    public function testSortByDescending()
    {
        $this->map->sortByDescending();

        $this->assertEquals('v3', $this->map->getAt(0));
        $this->assertEquals('v2', $this->map->getAt(1));
        $this->assertEquals('v1', $this->map->getAt(2));
    }

    public function testSortKeysBy()
    {
        $this->map->sortKeysBy(
                function($key1, $key2) {
                    return $key1 < $key2;
                }
        );

        $this->assertEquals('k3', $this->map->getKeyAt(0));
        $this->assertEquals('k2', $this->map->getKeyAt(1));
        $this->assertEquals('k1', $this->map->getKeyAt(2));
    }

    public function testSortKeysByAscending()
    {
        $this->map->sortKeysByAscending();

        $this->assertEquals('k1', $this->map->getKeyAt(0));
        $this->assertEquals('k2', $this->map->getKeyAt(1));
        $this->assertEquals('k3', $this->map->getKeyAt(2));
    }

    public function testSortKeysByDescending()
    {
        $this->map->sortKeysByDescending();

        $this->assertEquals('k3', $this->map->getKeyAt(0));
        $this->assertEquals('k2', $this->map->getKeyAt(1));
        $this->assertEquals('k1', $this->map->getKeyAt(2));
    }

    public function testGetByKey()
    {
        $this->assertEquals('v1', $this->map->getByKey('k1'));
        $this->assertEquals('v2', $this->map->getByKey('k2'));
        $this->assertEquals('v3', $this->map->getByKey('k3'));

        $this->setExpectedException('UnexpectedValueException');

        $this->map->getByKey('v6');
    }

    public function testGetKeyAt()
    {
        $this->assertEquals('k1', $this->map->getKeyAt(0));
        $this->assertEquals('k2', $this->map->getKeyAt(1));
        $this->assertEquals('k3', $this->map->getKeyAt(2));

        $this->setExpectedException('UnexpectedValueException');

        $this->map->getKeyAt(25);
    }

    public function testGetFirstKey()
    {
        $this->assertEquals('k1', $this->map->getFirstKey());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->clear();
        $this->map->getFirstKey();
    }

    public function testGetLastKey()
    {
        $this->assertEquals('k3', $this->map->getLastKey());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->clear();
        $this->map->getLastKey();
    }

    public function testContainsKey()
    {
        $this->assertTrue($this->map->containsKey('k1'));
        $this->assertTrue($this->map->containsKey('k2'));
        $this->assertTrue($this->map->containsKey('k3'));

        $this->assertFalse($this->map->containsKey('k42'));
        $this->assertFalse($this->map->containsKey(-25));
        $this->assertFalse($this->map->containsKey('v1'));
    }

    public function testRemoveByKey()
    {
        $this->map
                ->removeByKey('k1')
                ->removeByKey('k2')
                ->removeByKey('k3');

        $this->assertTrue($this->map->isEmpty());

        $this->setExpectedException('UnexpectedValueException');

        $this->map->removeByKey('k73');
    }

    public function testSet()
    {
        $this->map
                ->set('k1', 'v11')
                ->set('k2', 'v22')
                ->set('k3', 'v33');

        $this->assertEquals('v11', $this->map->getByKey('k1'));
        $this->assertEquals('v22', $this->map->getByKey('k2'));
        $this->assertEquals('v33', $this->map->getByKey('k3'));
    }

    public function testKeyOf()
    {
        $this->assertEquals('k1', $this->map->keyOf('v1'));
        $this->assertEquals('k2', $this->map->keyOf('v2'));
        $this->assertEquals('k3', $this->map->keyOf('v3'));

        $this->setExpectedException('UnexpectedValueException');

        $this->map->keyOf('v33');
    }

    public function testLastKeyOf()
    {
        $this->map->set('k4', 'v1');

        $this->assertEquals('k4', $this->map->lastKeyOf('v1'));
        $this->assertEquals('k2', $this->map->lastKeyOf('v2'));
        $this->assertEquals('k3', $this->map->lastKeyOf('v3'));

        $this->setExpectedException('UnexpectedValueException');

        $this->map->lastKeyOf('v33');
    }

}