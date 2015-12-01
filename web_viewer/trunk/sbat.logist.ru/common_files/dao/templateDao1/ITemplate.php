<?php
namespace DAO;

interface ITemplateEntity
{
    function selectTemplates();

    function selectTemplateByID($id);

    function updateTemplate($newTemplate);

    function deleteTemplate($Template);

    function addTemplate($Template);
}