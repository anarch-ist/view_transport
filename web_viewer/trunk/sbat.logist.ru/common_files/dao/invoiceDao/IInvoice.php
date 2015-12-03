<?php
namespace DAO;

interface IInvoiceEntity
{
    function selectInvoices();

    function selectInvoiceByID($id);

    function updateInvoice($newInvoice);

    function deleteInvoice($Invoice);

    function addInvoice($Invoice);

    function getInvoiceStatuses(\PrivilegedUser $pUser);
}